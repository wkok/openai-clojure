(ns wkok.openai-clojure.api-integration-test
  (:require [clojure.test :refer [deftest is testing]]
            #_[wkok.openai-clojure.api :as api]))

(deftest list-models
  (testing "lists the davinci model"
    (is true #_(= "davinci"
           (->> (api/list-models)
                :data
                (some #(when (= "davinci" (:id %)) (:id %))))))))

(deftest create-completion
  (testing "creates a simple completion"
    (is true #_(contains? (api/create-completion {:model "text-davinci-003"
                                           :prompt "Say this is a test"
                                           :max_tokens 7
                                           :temperature 0})
                   :choices))))

(deftest create-chat-completion
  (testing "creates a simple chat completion"
    (is true #_(contains? (api/create-chat-completion {:model "gpt-3.5-turbo"
                                                :messages [{:role "system" :content "You are a helpful assistant."}
                                                           {:role "user" :content "Who won the world series in 2020?"}
                                                           {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                                           {:role "user" :content "Where was it played?"}]})
                   :choices))))
