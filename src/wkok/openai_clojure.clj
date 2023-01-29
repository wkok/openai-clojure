(ns wkok.openai-clojure
  (:require [martian.core :as martian]
            [martian.clj-http :as martian-http]
            [martian.yaml :as yaml]
            [martian.openapi :as openapi]
            [clojure.java.io :as io]))

(def add-authentication-header
  {:name ::add-authentication-header
   :enter (fn [ctx]
            (assoc-in ctx
                      [:request :headers "Authorization"]
                      (str "Bearer " (System/getenv "OPEN_AI_API_KEY"))))})

(defn bootstrap-openapi
  []
  (let [definition (yaml/yaml->edn (slurp (io/resource "openapi.yaml")))
        base-url (openapi/base-url nil nil definition)
        opts (update martian-http/default-opts
                     :interceptors #(concat % [add-authentication-header]))]
    (martian/bootstrap-openapi base-url definition opts)))

(def m (bootstrap-openapi))

(defn response-for
  [operation]
  (-> (martian/response-for m operation)
      :body))

(defn make-fn
  [operation]
  (fn op
    [n]
    (response-for operation)))

(defn intern-fn
  [operation]
  (let [definition (martian/explore m operation)
        fn-name (-> operation name symbol
                    (with-meta {:doc (:summary definition)}))]
    (intern *ns* fn-name (make-fn operation))))

(defn def-operation-fns
  []
  (let [operations (->> (martian/explore m)
                        (map first))]
    (doseq [operation operations]
      (intern-fn operation))))

(def-operation-fns)


(comment

  (keys (ns-publics 'wkok.openai-clojure))

  (martian/explore m :list-engines)

  (list-engines 1)

  (meta list-engines)

  )
