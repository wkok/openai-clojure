(ns wkok.openai-clojure.azure
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.set :as set]
   [martian.clj-http :as martian-http]
   [martian.core :as martian]))

(def add-authentication-header
  {:name ::add-authentication-header
   :enter (fn [ctx]
            (assoc-in ctx [:request :headers "api-key"]
                      (System/getenv "AZURE_OPENAI_API_KEY")))})



(defn patch-handler [m]
  (let [completion-handler (-> m :handlers first)
        patched-handlers [(assoc completion-handler :route-name :create-completion)
                          (second (-> m :handlers first))]]
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

(defn patch-params [operation params]
  (case operation
    {:api-version (:api-version params)
     :deployment-id (:model params)
     :martian.core/body (select-keys params (set/difference
                                             (into #{} (keys params))
                                             #{:api-version :model}))}))
