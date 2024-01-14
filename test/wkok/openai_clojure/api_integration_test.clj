(ns wkok.openai-clojure.api-integration-test
  (:require [clojure.test :refer [deftest is testing]]
            [wkok.openai-clojure.api :as api]))

(deftest create-chat-completion
  (testing "creates a simple chat completion"
    (is (contains? (api/create-chat-completion {:model "gpt-3.5-turbo"
                                                :messages [{:role "system" :content "You are a helpful assistant."}
                                                           {:role "user" :content "Who won the world series in 2020?"}
                                                           {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                                           {:role "user" :content "Where was it played?"}]})
                   :choices))))
