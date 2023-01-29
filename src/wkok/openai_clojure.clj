(ns wkok.openai-clojure
  (:require [martian.core :as martian]
            [martian.clj-http :as martian-http]
            [martian.yaml :as yaml]
            [martian.openapi :as openapi]
            [clojure.java.io :as io]))

(def ^:no-doc add-authentication-header
  {:name ::add-authentication-header
   :enter (fn [ctx]
            (assoc-in ctx
                      [:request :headers "Authorization"]
                      (str "Bearer " (System/getenv "OPEN_AI_API_KEY"))))})

(defn ^:no-doc bootstrap-openapi
  "Bootstrap the martian from a local copy of the openai swagger spec"
  []
  (let [definition (yaml/yaml->edn (slurp (io/resource "openapi.yaml")))
        base-url (openapi/base-url nil nil definition)
        opts (update martian-http/default-opts
                     :interceptors #(concat % [add-authentication-header]))]
    (martian/bootstrap-openapi base-url definition opts)))

(def ^:no-doc m (bootstrap-openapi))

(defn ^:no-doc response-for
  [operation]
  (-> (martian/response-for m operation)
      :body))

(defn ^:no-doc make-fn
  "Creates a clojure function using the swagger operation / endpoint definition"
  [operation]
  (fn op
    [n]
    (response-for operation)))

(defn ^:no-doc intern-fn
  "Dynamically defines a clojure function in the api namespace, using
  the swagger operation / endpoint definition"
  [operation]
  (let [definition (martian/explore m operation)
        fn-name (-> operation name symbol
                    (with-meta {:doc (:summary definition)}))]
    (when-not (:deprecated definition)
      (intern *ns* fn-name (make-fn operation)))))

(defn ^:no-doc def-operation-fns
  "Dynamically define clojure functions for all operations / endpoints
  defined in the swagger definition"
  []
  (let [operations (->> (martian/explore m)
                        (map first))]
    (doseq [operation operations]
      (intern-fn operation))))

(def-operation-fns)
