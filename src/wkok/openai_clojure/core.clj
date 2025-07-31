(ns ^:no-doc wkok.openai-clojure.core
  (:require
   [martian.core :as martian]
   [wkok.openai-clojure.azure :as azure]
   [wkok.openai-clojure.openai :as openai]))

(defn transform-deprecated-args?
  "Earlier versions of the openai-clojure api supported passing the impl as the
  first argument. This transforms these args into the new format"
  [params options]
  (when (keyword? params) ;; was either :openai or :azure
    (println "\nWARNING - passing impl as first argument is deprecated, use options instead\n")
    {:p options
     :o {:impl params}}))

(defn response-for
  [operation params
   {:keys [impl request full-response?]
    :or {impl :openai
         full-response? false}
    :as options}]

  (if-let [{:keys [p o]} (transform-deprecated-args? params options)]

    (response-for operation p o)

    (let [m (case impl
              :openai @openai/m
              :azure @azure/m)

          patch-parms-fn (case impl
                           :openai identity
                           :azure azure/patch-params)

          patched-params (patch-parms-fn params)

          response (martian/response-for m operation (assoc patched-params ::options options))]

      (if (:async? request)
        response
        (if full-response?
          response
          (:body response))))))
