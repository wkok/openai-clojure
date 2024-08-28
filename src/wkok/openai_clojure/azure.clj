(ns ^:no-doc wkok.openai-clojure.azure
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.string :as s]
   [martian.core :as martian]
   [martian.hato :as martian-http]
   [wkok.openai-clojure.interceptors :as openai-interceptors]
   [wkok.openai-clojure.sse :as sse]))

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

(defn- ->patched-handler
  [m from to]
  (->  (martian/handler-for  m :completions-create)
       (assoc :route-name :create-completion)))

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

(def m
  (delay
    (patch-handler
      (martian/bootstrap-openapi "/openai"
                                 (load-openai-spec)
                                 (update
                                   martian-http/default-opts
                                   :interceptors
                                   #(-> (remove (comp #{martian-http/perform-request}) %)
                                        (concat [add-authentication-header
                                                 openai-interceptors/set-request-options
                                                 override-api-endpoint
                                                 sse/perform-sse-capable-request])))))))

(defn patch-params [params]
  {:api-version "2024-06-01"
   :deployment-id (:model params)
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
