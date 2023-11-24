(ns ^:no-doc wkok.openai-clojure.openai
  (:require
    [clojure.java.io :as io]
    [martian.hato :as martian-http]
    [martian.core :as martian]
    [martian.openapi :as openapi]
    [martian.yaml :as yaml]
    [wkok.openai-clojure.sse :as sse]
    [wkok.openai-clojure.interceptors :as openai-interceptors]
    [martian.encoders :as encoders]
    [martian.interceptors :as interceptors]
    [schema.core :as s]))

(def add-headers
  {:name  ::add-headers
   :enter (fn [ctx]
            (let [api-key (or (-> ctx :params :wkok.openai-clojure.core/options :api-key)
                              (System/getenv "OPENAI_API_KEY"))
                  organization (or (-> ctx :params :wkok.openai-clojure.core/options :organization)
                                   (System/getenv "OPENAI_ORGANIZATION"))
                  openai-beta (-> ctx :params :wkok.openai-clojure.core/options :openai-beta)]
              (update-in ctx [:request :headers]
                         (fn [headers]
                           (cond-> headers
                                   (not-empty api-key) (assoc "Authorization" (str "Bearer " api-key))
                                   (not-empty organization) (assoc "OpenAI-Organization" organization)
                                   (not-empty openai-beta)  (assoc "OpenAI-Beta" openai-beta))))))})

(defn override-api-endpoint
  [base-url]
  {:name  ::override-api-endpoint
   :enter (fn [ctx]
            (update-in ctx [:request :url]
                       (fn [url]
                         (let [endpoint (or (-> ctx :params :wkok.openai-clojure.core/options :api-endpoint)
                                            (System/getenv "OPENAI_API_ENDPOINT")
                                            base-url)]
                           (str endpoint (subs url (count base-url)))))))})

(defn- multipart-form-data?
  [handler]
  (-> handler :openapi-definition :requestBody :content :multipart/form-data))

(defn- param->multipart-entry
  [[param content]]
  {:name    (name param)
   :content (if (or (instance? java.io.File content)
                    (instance? java.io.InputStream content)
                    (bytes? content))
              content
              (str content))})

(def multipart-form-data
  {:name  ::multipart-form-data
   :enter (fn [{:keys [handler params] :as ctx}]
            (let [params' (dissoc params :wkok.openai-clojure.core/options)]
              (if (multipart-form-data? handler)
                (-> (assoc-in ctx [:request :multipart]
                              (map param->multipart-entry params'))
                    (update-in [:request :headers] dissoc "Content-Type")
                    (update :request dissoc :body))
                ctx)))})

(defn update-file-schema
  [m operation-id field-name]
  (martian/update-handler m operation-id assoc-in [:body-schema :body field-name] java.io.File))

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
  (let [definition (yaml/yaml->edn (slurp (io/resource "openapi.yaml")))
        base-url (openapi/base-url nil nil definition)
        encoders (assoc (encoders/default-encoders)
                   "multipart/form-data" nil)
        opts (update martian-http/default-opts
                     :interceptors (fn [interceptors]
                                     (-> (remove #(#{:martian.hato/perform-request} (:name %))
                                                 interceptors)
                                         (concat [add-headers
                                                  openai-interceptors/set-request-options
                                                  (override-api-endpoint base-url)
                                                  (interceptors/encode-body encoders)
                                                  multipart-form-data
                                                  sse/perform-sse-capable-request]))))]
    (-> (martian/bootstrap-openapi base-url definition opts)
        update-file-schemas)))

(def m (delay (bootstrap-openapi)))
