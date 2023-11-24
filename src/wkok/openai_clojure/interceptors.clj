(ns ^:no-doc wkok.openai-clojure.interceptors)

(def set-request-options
  {:name ::set-request-options
   :enter (fn [{{{request :request} :wkok.openai-clojure.core/options} :params
                :as ctx}]
            (update ctx :request merge request))})
