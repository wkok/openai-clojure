(ns ^:no-doc wkok.openai-clojure.azure
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.string :as s]
   [martian.core :as martian]
   [martian.hato :as martian-http]
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

(defn patch-handler [m]
  ;; patching works for API version "2022-12-01"
  (let [patched-completions-create-handler (->  (martian/handler-for  m :completions-create)
                                                (assoc :route-name :create-completion))
        patched-embeddings-create-handler (->  (martian/handler-for  m :embeddings-create)
                                               (assoc :route-name :create-embedding))


        patched-handlers [patched-completions-create-handler
                          patched-embeddings-create-handler]]

    (assoc m :handlers patched-handlers)))

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
                                      (concat [add-authentication-header override-api-endpoint sse/perform-sse-capable-request])))))))

(defn patch-params [params]
  {:api-version "2022-12-01"
   :deployment-id (:model params)
   :martian.core/body (dissoc params :model)})
