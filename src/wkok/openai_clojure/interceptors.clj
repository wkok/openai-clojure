(ns wkok.openai-clojure.interceptors)

(def set-request-options
  {:name ::method
   :enter (fn [{{{request :request} :wkok.openai-clojure.core/options} :params
                :as ctx}]
            (update ctx :request merge request))})
