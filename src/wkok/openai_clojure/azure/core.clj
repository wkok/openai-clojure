(ns wkok.openai-clojure.azure.core
  (:require
     [clojure.java.io :as io]
     [martian.clj-http :as martian-http]
     [martian.core :as martian]
     [cheshire.core :as json]))



(def add-authentication-header
  {:name ::add-authentication-header
   :enter (fn [ctx]
            (assoc-in ctx [:request :headers "api-key"]
                      (System/getenv "AZURE_OPENAI_API_KEY")))})
                      


(def m
  (martian/bootstrap-openapi (format "%s/openai" (System/getenv "AZURE_OPENAI_API_ENDPOINT"))


                             (json/decode (slurp (io/resource "azure_openai.json")) keyword)
                             (update
                              martian-http/default-opts
                              :interceptors
                              (fn [s] (concat [add-authentication-header] s)))))




(defn response-for
  [operation params]
  (-> (martian/response-for m operation params)
      :body))
