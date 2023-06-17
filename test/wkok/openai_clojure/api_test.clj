(ns wkok.openai-clojure.api-test
  (:require
   [cheshire.core :as json]
   [clojure.core.async :as a]
   [clojure.java.io :as io]
   [clojure.test :refer [deftest is testing]]
   [hato.client :as http]
   [martian.core :as martian]
   [martian.hato :as martian-http]
   [wkok.openai-clojure.api :as api]
   [wkok.openai-clojure.azure :as azure]
   [wkok.openai-clojure.openai :as openai]
   [wkok.openai-clojure.sse :as sse]))

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
              (api/create-completion  {:model "text-davinci-003"
                                       :prompt "Say this is a test"
                                       :max_tokens 7
                                       :temperature 0}
                                      {:impl :azure})))

       (is (= :success
              (api/create-chat-completion {:model "gpt-3.5-turbo"
                                           :messages [{:role "system" :content "You are a helpful assistant."}
                                                      {:role "user" :content "Who won the world series in 2020?"}
                                                      {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                                      {:role "user" :content "Where was it played?"}]})))

       (is (= :success
              (api/qcreate-chat-completion {:model    "gpt-3.5-turbo-0613"
                                           :messages [{:role    "user"
                                                       :content "Wikipedia page about foxes"}]
                                           :functions
                                           [{:name        "get_current_weather"
                                             :description "Get the current weather in a given location"
                                             :parameters
                                             {:type       "object"
                                              :properties {:location {:type        "string"
                                                                      :description "The city and state, e.g. San Francisco, CA"}
                                                           :unit     {:type "string"
                                                                      :enum ["celsius" "fahrenheit"]}}}}]})))

       (is (= :success
              (api/create-chat-completion {:model "gpt-3.5-turbo"
                                           :messages [{:role "system" :content "You are a helpful assistant."}
                                                      {:role "user" :content "Who won the world series in 2020?"}
                                                      {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                                      {:role "user" :content "Where was it played?"}]}
                                          {:impl :azure})))

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
              (api/create-embedding  {:model "text-embedding-ada-002"
                                      :input "The food was delicious and the waiter..."}
                                     {:impl :azure})))

       (is (= :success
              (api/create-transcription {:file (io/file "path/to/audio.mp3")
                                         :model "whisper-1"})))

       (is (= :success
              (api/create-translation {:file (io/file "path/to/file/german.m4a")
                                       :model "whisper-1"})))

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

  (testing "streamed event is returned in the correct format for chat"

    (let [event {:id "chatcmpl-6srv5jx3p4I9deNDzU7ucNXKoGS0L"
                 :object "chat.completion.chunk"
                 :created 1678534999
                 :model "gpt-3.5-turbo-0301"
                 :choices [{:delta {:content "The"} :index 0, :finish_reason nil}]}
          http-fn (fn [_] {:body (io/input-stream (.getBytes (str "data: " (json/generate-string event)  "\n\n")))})
          events (with-redefs [http/request http-fn]
                   (api/create-chat-completion {:model "gpt-3.5-turbo"
                                                :messages [{:role "system" :content "You are a helpful assistant."}
                                                           {:role "user" :content "Who won the world series in 2020?"}
                                                           {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                                           {:role "user" :content "Where was it played?"}]
                                                :stream true}))]

      (is (= event (a/<!! events)))))

  (testing "streamed event is returned in the correct format for completions"

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

(deftest multipart-test
  (let [multipart (-> (martian/request-for @openai/m :create-transcription {:file (io/file "path/to/audio.mp3")
                                                                            :model "whisper-1"})
                      :multipart)]
    (is (some #(#{"file"} (:name %)) multipart))
    (is (some #(#{"model"} (:name %)) multipart))))
