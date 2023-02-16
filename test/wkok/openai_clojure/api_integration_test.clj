(ns wkok.openai-clojure.api-integration-test
  (:require [clojure.test :refer [deftest is testing]]
            [wkok.openai-clojure.api :as api]))

(deftest list-models
  (testing "lists the davinci model"
    (is (= "davinci"
           (->> (api/list-models)
                :data
                (some #(when (= "davinci" (:id %)) (:id %))))))))

(deftest create-completion
  (testing "creates a simple completion"
    (is (contains? (api/create-completion {:model "text-davinci-003"
                                           :prompt "Say this is a test"
                                           :max_tokens 7
                                           :temperature 0})
                   :choices))))
