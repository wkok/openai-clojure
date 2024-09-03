(ns ^:no-doc wkok.openai-clojure.azure
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.string :as s]
   [martian.core :as martian]
   [martian.hato :as martian-http]
   [wkok.openai-clojure.interceptors :as openai-interceptors]
   [wkok.openai-clojure.sse :as sse]
   [martian.interceptors :as interceptors]
   [martian.encoders :as encoders]))

(def add-authentication-header
  {:name ::add-authentication-header
   :enter (fn [ctx]
            (let [api-key (or (-> ctx :params :wkok.openai-clojure.core/options :api-key)
                              (System/getenv "AZURE_OPENAI_API_KEY"))]
              (assoc-in ctx [:request :headers "api-key"]
                        api-key)))})

(def override-api-endpoint
  {:name ::override-api-endpoint
   :enter (fn [ctx]
            (update-in ctx [:request :url]
                       (fn [url]
                         (let [endpoint (or (-> ctx :params :wkok.openai-clojure.core/options :api-endpoint)
                                            (System/getenv "AZURE_OPENAI_API_ENDPOINT"))
                               idx (s/index-of url "/openai")]
                           (str endpoint (subs url idx))))))})

(def route-mappings
  {:completions-create :create-completion
   :chat-completions-create :create-chat-completion
   :embeddings-create :create-embedding
   :transcriptions-create :create-transcription
   :translations-create :create-translation
   :image-generations-create :create-image})

(defn patch-handler
  "Patching azure's handlers to support the same operation-id names as the standard openai api"
  [m]
  (update m :handlers
          (fn [handlers]
            (map (fn [{route-name :route-name :as handler}]
                   (update handler :route-name #(route-name route-mappings %)))
                 handlers))))

(defn load-openai-spec []
  (json/decode (slurp (io/resource "azure_openai.json")) keyword))

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
            (let [params' (:martian.core/body params)]
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
      (update-file-schema :transcriptions-create :file)
      (update-file-schema :translations-create :file)))


(defn bootstrap-openapi
  "Bootstrap the martian from a local copy of the openai swagger spec"
  []
  (let [definition (load-openai-spec)
        base-url   "/openai"
        encoders   (assoc (encoders/default-encoders)
                          "multipart/form-data" nil
                          "application/octet-stream" nil)
        opts       (update martian-http/default-opts
                           :interceptors (fn [interceptors]
                                           (-> interceptors
                                               (interceptors/inject
                                                 add-authentication-header
                                                 :after
                                                 :martian.interceptors/header-params)
                                               (interceptors/inject
                                                 multipart-form-data
                                                 :after
                                                 ::add-authentication-header)
                                               (interceptors/inject
                                                 openai-interceptors/set-request-options
                                                 :before
                                                 :martian.hato/perform-request)
                                               (interceptors/inject
                                                 override-api-endpoint
                                                 :before
                                                 :martian.hato/perform-request)
                                               (interceptors/inject
                                                 sse/perform-sse-capable-request
                                                 :replace
                                                 :martian.hato/perform-request)
                                               (interceptors/inject
                                                 (interceptors/encode-body encoders)
                                                 :replace
                                                 :martian.interceptors/encode-body)
                                               (interceptors/inject
                                                 (interceptors/coerce-response encoders)
                                                 :replace
                                                 :martian.interceptors/coerce-response))))]

    (-> (martian/bootstrap-openapi base-url definition opts)
        update-file-schemas)))

(def m
  (delay
    (patch-handler
      (bootstrap-openapi))))

(defn patch-params [params]
  {:api-version       "2024-06-01"
   :deployment-id     (:model params)
   :martian.core/body (dissoc params :model)})


(comment

  ;; Get all operations defined in spec

  (def spec (->>  (martian/explore @m)
                  (map first)
                  #_(map #(martian/explore @m %))
                  set))

  ;; Get all functions defined in api

  (require 'wkok.openai-clojure.api)

  (def impl (->>  (keys (ns-publics 'wkok.openai-clojure.api))
                  (map keyword)
                  set))

  ;; Compare the two

  (clojure.set/difference impl spec)
  )
