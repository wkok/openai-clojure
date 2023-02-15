(ns wkok.openai-clojure-test
  (:require [clojure.test :refer :all]
            [wkok.openai-clojure.api :as api]
            ))

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


(comment
  (api/create-completion {:model "text-davinci-003"
                          :prompt "Say this is a test"
                          :max_tokens 7
                          :temperature 0})

  (api/create-completion :openai {:model "text-davinci-003"
                                  :prompt "Say this is a test"
                                  :max_tokens 7
                                  :temperature 0})


  (api/create-completion :azure
                         {:model "testtextdavanci003"
                          :prompt "How are you ?"
                          :max_tokens 100
                          :temperature 0})
  (api/create-embedding :azure {:model "textembeddingada"
                                :input "How are you ?"}))




                          
