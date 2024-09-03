(ns wkok.openai-clojure.sse-test
  (:require
   [cheshire.core :as json]
   [clojure.core.async :as a]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [hato.client :as http]
   [wkok.openai-clojure.sse :as sse])
  (:import (java.io PipedInputStream PipedOutputStream)))

(defn- generate-events
  [data-coll]
  (let [events (map #(str "data: " (json/generate-string %)) data-coll)]
    (str/join
     (eduction
      cat
      (map #(str % "\n\n"))
      [events ["data: [DONE]"]]))))

(defn- stream-string
  [^PipedOutputStream output-stream ^String s]
  (future
    (doseq [c (seq (.getBytes s))]
      (.write output-stream ^int c)
      (.flush output-stream)
      (Thread/sleep 1))
    (.close output-stream)))

(deftest sse-events-test
  (testing "channel can get events"
    (let [test-data [{:text "hello"} {:text "world"}]
          test-events (generate-events test-data)]
      (with-open [output-stream (PipedOutputStream.)
                  input-stream (PipedInputStream. output-stream)]
        (with-redefs [http/request (constantly {:body input-stream})]
          (let [events (sse/sse-events {})]
            (stream-string output-stream test-events)
            (is (= (first test-data)
                   (a/<!! events)))
            (is (= (second test-data)
                   (a/<!! events)))
            (is (= :done
                   (a/<!! events)))
            (is (= nil
                   (a/poll! events))))))))

  (testing "channel events with `stream/close?` parameter"
    (let [test-data [{:text "hello"} {:text "world"}]
          test-events (generate-events test-data)]
      (with-open [output-stream (PipedOutputStream.)
                  input-stream (PipedInputStream. output-stream)]
        (with-redefs [http/request (constantly {:body input-stream})]
          (let [events (sse/sse-events {:params {:stream/close? true}})]
            (stream-string output-stream test-events)
            (is (= (first test-data)
                   (a/<!! events)))
            (is (= (second test-data)
                   (a/<!! events)))
            (is (= :done
                   (a/<!! events)))
            (is (= nil
                   (a/<!! events))))))))

  (testing "channel events with `:on-next`"
    (doseq [close? [true false]]
      (let [test-data [{:text "hello"} {:text "world"}]
            test-events (generate-events test-data)]
        (with-open [output-stream (PipedOutputStream.)
                    input-stream (PipedInputStream. output-stream)]
          (with-redefs [http/request (constantly {:body input-stream})]
            (let [events (sse/sse-events {:stream/close? close?})
                  results (promise)
                  ;; accumulate results and fulfill promise when done
                  on-next (let [acc (volatile! [])]
                            (fn [x]
                              (vswap! acc conj x)
                              (when (= x (last test-data))
                                (deliver results @acc))))]
              (sse/deliver-events events {:on-next on-next})
              (stream-string output-stream test-events)
              (is (= test-data
                     @results))))))))

  (testing "support multibytes"
    (let [test-data [{:text "こんにちは"} {:text "你好"}]
          test-events (generate-events test-data)]
      (with-open [output-stream (PipedOutputStream.)
                  input-stream (PipedInputStream. output-stream)]
        (with-redefs [http/request (constantly {:body input-stream})]
          (let [events (sse/sse-events {})]
            (stream-string output-stream test-events)
            (is (= (first test-data)
                   (a/<!! events)))
            (is (= (second test-data)
                   (a/<!! events)))))))))
