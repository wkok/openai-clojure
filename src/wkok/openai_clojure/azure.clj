(ns ^:no-doc wkok.openai-clojure.azure
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [martian.clj-http :as martian-http]
   [martian.core :as martian]))

(def add-authentication-header
  {:name ::add-authentication-header
   :enter (fn [ctx]
            (assoc-in ctx [:request :headers "api-key"]
                      (System/getenv "AZURE_OPENAI_API_KEY")))})


(defn patch-handler [m]
  ;; patching works for API version "2022-12-01"
  (let [patched-completions-create-handler (->  (martian/handler-for  m :completions-create)
                                                (assoc :route-name :create-completion))
        patched-embeddings-create-handler (->  (martian/handler-for  m :embeddings-create)
                                               (assoc :route-name :create-embedding))


        patched-handlers [patched-completions-create-handler
                          patched-embeddings-create-handler]]

    (assoc m :handlers patched-handlers)))


(def m
  (delay
    (patch-handler
     (martian/bootstrap-openapi (format "%s/openai" (System/getenv "AZURE_OPENAI_API_ENDPOINT"))
                                (json/decode (slurp (io/resource "azure_openai.json")) keyword)
                                (update
                                 martian-http/default-opts
                                 :interceptors
                                 (fn [s] (concat [add-authentication-header] s)))))))

(defn patch-params [params]
  {:api-version "2022-12-01"
   :deployment-id (:model params)
   :martian.core/body (dissoc params :model)})
