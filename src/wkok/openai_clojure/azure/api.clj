(ns wkok.openai-clojure.azure.api
  (:require [wkok.openai-clojure.azure.core :as core]))


(defn create-completion
  "Creates a completion for the provided prompt and parameters

  Example:
  ```
  (create-completion {:martian.core/body {:prompt \"How are you ?\"
                                          :max_tokens 100}
                      :deployment-id \"testtextdavanci003\"
                      :api-version \"2022-12-01\"})

  ```
  Also see the [Azure OpenAI documentation]https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#completions
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :completions-create params))


(comment
  (create-completion {:martian.core/body {:prompt "How are you ?"
                                          :max_tokens 100}
                      :deployment-id "testtextdavanci003"
                      :api-version "2022-12-01"}))

;; => {:id "cmpl-6jxrzB6in83RE2XRANbCWpq3GIX8C",
;;     :object "text_completion",
;;     :created 1676412919,
;;     :model "text-davinci-003",
;;     :choices
;;     [{:text "\n\nI'm good, thank you. How about you?",
;;       :index 0,
;;       :logprobs nil,
;;       :finish_reason "stop"}],
;;     :usage {:prompt_tokens 4, :completion_tokens 13, :total_tokens 17}})
