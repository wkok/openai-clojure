(ns wkok.openai-clojure
  (:require
   [clojure.java.io :as io]
   [clojure.pprint :as pprint]
   [martian.clj-http :as martian-http]
   [martian.core :as martian]
   [martian.openapi :as openapi]
   [martian.yaml :as yaml]))

(def ^:no-doc add-headers
  {:name ::add-headers
   :enter (fn [ctx]
            (let [api-key (System/getenv "OPENAI_API_KEY")
                  organization (System/getenv "OPENAI_ORGANIZATION")]
              (update-in ctx [:request :headers]
                         (fn [headers]
                           (cond-> headers
                             (not-empty api-key) (assoc "Authorization" (str "Bearer " api-key))
                             (not-empty organization) (assoc "OpenAI-Organization" organization))))))})

(defn ^:no-doc bootstrap-openapi
  "Bootstrap the martian from a local copy of the openai swagger spec"
  []
  (let [definition (yaml/yaml->edn (slurp (io/resource "openapi.yaml")))
        base-url (openapi/base-url nil nil definition)
        opts (update martian-http/default-opts
                     :interceptors #(concat % [add-headers]))]
    (martian/bootstrap-openapi base-url definition opts)))

(def ^:no-doc m (bootstrap-openapi))

(defn ^:no-doc response-for
  [operation params]
  (-> (martian/response-for m operation params)
      :body))

(defn ^:no-doc make-fn
  "Creates a clojure function using the swagger operation / endpoint definition"
  [operation definition]
  (fn
    ([]
     (response-for operation {}))
    ([params]
     (response-for operation params))))

(defn- definition->docstring
  [definition]
  (str (:summary definition)
       "\n\n"
       (if (-> definition :parameters empty?)
         "*Zero arguments.*"
         (str "*Argument map:*"
              "\n\n"
              "```"
              "\n"
              (if (contains? (:parameters definition) :body)
                (-> definition :parameters :body pprint/pprint with-out-str)
                (-> definition :parameters pprint/pprint with-out-str))
              "\n"
              "```"))
       "\n\n"
       "*Returns:*"
       "\n\n"
       "```"
       "\n"
       (-> definition :returns (get 200) pprint/pprint with-out-str)
       "\n"
       "```"))

(defn ^:no-doc intern-fn
  "Dynamically defines a clojure function in the api namespace, using
  the swagger operation / endpoint definition"
  [operation]
  (let [definition (martian/explore m operation)
        fn-name (-> operation name symbol
                    (with-meta {:doc/format :markdown
                                :doc (definition->docstring definition)}))]
    (when-not (:deprecated definition)
      (intern *ns* fn-name (make-fn operation definition)))))

(defn ^:no-doc def-operation-fns
  "Dynamically define clojure functions for all operations / endpoints
  defined in the swagger definition"
  []
  (let [operations (->> (martian/explore m)
                        (map first))]
    (doseq [operation operations]
      (intern-fn operation))))

(def-operation-fns)

(comment

  (martian/explore m :create-file)

  (create-completion {:model "text-davinci-003"
                             :pompt "Say this is a test"
                             :stream false})

  (list-files)
  (retrieve-model {:model "text-davinci-003"})
  (create-image {:body {:prompt "boy sitting under a tree"}})
  )
