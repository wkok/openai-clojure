(ns ^:no-doc wkok.openai-clojure.openai
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [wkok.openai-clojure.macros :refer [inline-resource-yaml]])
  (:require
   #?(:clj [clojure.java.io :as io])
   #?(:clj [cheshire.core :as json])
   #?(:clj [martian.hato :as martian-http])
   #?(:clj [martian.yaml :as yaml])
   #?(:cljs [martian.cljs-http :as martian-http])
   [martian.core :as martian]
   [martian.openapi :as openapi]
   [wkok.openai-clojure.sse :as sse]
   [martian.encoders :as encoders]
   [martian.interceptors :as interceptors]
   [schema.core :as s]
   [cljs.core.async :refer [<!]]))

(def add-headers
  {:name ::add-headers
   :enter (fn [ctx]
            (let [api-key (or (-> ctx :params :wkok.openai-clojure.core/options :api-key)
                              #?(:clj (System/getenv "OPENAI_API_KEY")
                                 :cljs ""))
                  organization (or (-> ctx :params :wkok.openai-clojure.core/options :organization)
                                   #?(:clj (System/getenv "OPENAI_ORGANIZATION")
                                      :cljs ""))]
              (update-in ctx [:request :headers]
                         (fn [headers]
                           (cond-> headers
                             (not-empty api-key) (assoc "Authorization" (str "Bearer " api-key))
                             (not-empty organization) (assoc "OpenAI-Organization" organization))))))})

(defn- multipart-form-data?
  [handler]
  (-> handler :openapi-definition :requestBody :content :multipart/form-data))

(defn- param->multipart-entry
  [[param content]]
  {:name (name param)
   :content #?(:clj (if (or (instance? java.io.File content)
                            (instance? java.io.InputStream content)
                            (bytes? content))
                      content
                      (str content))
               :cljs (str content))})

(def multipart-form-data
  {:name ::multipart-form-data
   :enter (fn [{:keys [handler params] :as ctx}]
            (if (multipart-form-data? handler)
              (-> (assoc-in ctx [:request :multipart]
                            (map param->multipart-entry params))
                  (update-in [:request :headers] dissoc "Content-Type")
                  (update :request dissoc :body))
              ctx))})

(defn update-file-schema
  [m operation-id field-name]
  (martian/update-handler m operation-id assoc-in [:body-schema :body field-name] #?(:clj java.io.File)))

(defn update-file-schemas
  [m]
  (-> m
      (update-file-schema :create-transcription :file)
      (update-file-schema :create-translation :file)
      (update-file-schema :create-file :file)
      (update-file-schema :create-image-edit :image)
      (update-file-schema :create-image-edit (schema.core/optional-key :mask))
      (update-file-schema :create-image-variation :image)))

(defn bootstrap-openapi
  "Bootstrap the martian from a local copy of the openai swagger spec"
  []
  (let [definition #?(:clj (yaml/yaml->edn (slurp (io/resource "openapi.yaml")))
                      :cljs (inline-resource-yaml "openapi.yaml"))
        base-url #?(:clj (openapi/base-url nil nil definition)
                    :cljs "")
        encoders (assoc (encoders/default-encoders)
                        "multipart/form-data" nil)
        opts (update martian-http/default-opts
                     :interceptors (fn [interceptors]
                                     (-> (remove #(#{:martian.hato/perform-request} (:name %))
                                                 interceptors)
                                         (concat [add-headers
                                                  (interceptors/encode-body encoders)
                                                  multipart-form-data
                                                  sse/perform-sse-capable-request]))))]
    (-> (martian/bootstrap-openapi base-url definition opts)
        update-file-schemas)))

(def m (delay (bootstrap-openapi)))
