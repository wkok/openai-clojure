(ns ^:no-doc wkok.openai-clojure.core
  (:require
   [martian.core :as martian]
   [wkok.openai-clojure.azure :as azure]
   [wkok.openai-clojure.openai :as openai]))





(defn response-for
  [implementation operation params]
  (let [m
        (case implementation
          :openai @openai/m
          :azure @azure/m)

        patch-parms-fn
        (case :openai openai/noop-patch-params
              :azure azure/patch-params)]


    (-> (martian/response-for m operation (patch-parms-fn operation params))
        :body)))
