(ns wkok.openai-clojure.api-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer [deftest is testing]]
   [martian.hato :as martian-http]
   [wkok.openai-clojure.api :as api]
   [wkok.openai-clojure.azure :as azure]
   [wkok.openai-clojure.openai :as openai]
   [wkok.openai-clojure.sse :as sse]
   [clojure.core.async :as a]
   [cheshire.core :as json]
   [hato.client :as http]))

(def openai-martian @openai/m)

(def azure-martian @azure/m)

(def success-response
  {:name ::success-response
   :leave #(assoc % :response {:status 200 :body :success})})

(defn stub-martian
  "Stub the martian with an interceptor that will always respond
  with success instead of doing an actual http request"
  [m]
  (update m :interceptors
          (fn [interceptors]
            (-> (remove (comp #{martian-http/perform-request
                                sse/perform-sse-capable-request})
                        interceptors)
                (concat [success-response])))))

(defn with-stubbed-martians
  [f]
  (with-redefs [openai/m (delay (stub-martian openai-martian))
                azure/m (delay (stub-martian azure-martian))]
    (f)))

(deftest api-test

  (with-stubbed-martians

    #(testing "example api requests successfully execute with bootstrapped martian validation"

       (is (= :success
              (api/list-models)))

       (is (= :success
              (api/retrieve-model {:model "text-davinci-003"})))

       (is (= :success
              (api/create-completion {:model "text-davinci-003"
                                      :prompt "Say this is a test"
                                      :max_tokens 7
                                      :temperature 0})))

       (is (= :success
              (api/create-completion :azure {:model "text-davinci-003"
                                             :prompt "Say this is a test"
                                             :max_tokens 7
                                             :temperature 0})))

       (is (= :success
              (api/create-edit {:model "text-davinci-edit-001"
                                :input "What day of the wek is it?"
                                :instruction "Fix the spelling mistakes"})))

       (is (= :success
              (api/create-image {:prompt "A cute baby sea otter"
                                 :n 2
                                 :size "1024x1024"})))

       (is (= :success
              (api/create-image-edit {:image (io/file "path/to/otter.png")
                                      :mask (io/file "path/to/mask.png")
                                      :prompt "A cute baby sea otter wearing a beret"
                                      :n 2
                                      :size "1024x1024"})))

       (is (= :success
              (api/create-image-variation {:image (io/file "path/to/otter.png")
                                           :n 2
                                           :size "1024x1024"})))

       (is (= :success
              (api/create-embedding {:model "text-embedding-ada-002"
                                     :input "The food was delicious and the waiter..."})))

       (is (= :success
              (api/create-embedding :azure {:model "text-embedding-ada-002"
                                            :input "The food was delicious and the waiter..."})))

       (is (= :success
              (api/list-files)))

       (is (= :success
              (api/create-file {:purpose "fine-tune"
                                :file (io/file "path/to/fine-tune.jsonl")})))

       (is (= :success
              (api/delete-file {:file-id "file-wefuhweof"})))

       (is (= :success
              (api/retrieve-file {:file-id "file-wefuhweof"})))

       (is (= :success
              (api/download-file {:file-id "file-wefuhweof"})))

       (is (= :success
              (api/create-fine-tune {:training_file "file-xuhfiwuefb"})))

       (is (= :success
              (api/list-fine-tunes)))

       (is (= :success
              (api/retrieve-fine-tune {:fine_tune_id "ft-1wefweub"})))

       (is (= :success
              (api/cancel-fine-tune {:fine_tune_id "ft-1wefweub"})))

       (is (= :success
              (api/list-fine-tune-events {:fine_tune_id "ft-1wefweub"})))

       (is (= :success
              (api/delete-model {:model "fine-tune"})))

       (is (= :success
              (api/create-moderation {:input "I want to kill them"}))))))

(deftest stream-test

  (testing "streamed event is returned in the correct format"

    (let [event {:id "cmpl-6lfczGsgHJi6B2pAtN4e91frDXRuM",
                 :object "text_completion",
                 :created 1676819453,
                 :choices
                 [{:text "This", :index 0, :logprobs nil, :finish_reason nil}],
                 :model "text-davinci-003"}
          http-fn (fn [_] {:body (io/input-stream (.getBytes (str "data: " (json/generate-string event)  "\n\n")))})
          events (with-redefs [http/request http-fn]
                   (api/create-completion {:model "text-davinci-003"
                                           :prompt "Say this is a test"
                                           :max_tokens 7
                                           :temperature 0
                                           :stream true}))]

      (is (= event (a/<!! events))))))
