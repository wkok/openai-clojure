(ns ^:no-doc wkok.openai-clojure.sse
  (:require
   #?(:clj [hato.client :as http])
   [clojure.core.async :as a]
   [clojure.string :as string]
   #?(:clj [cheshire.core :as json]))
  #?(:clj (:import (java.io InputStream))))

(def event-mask (re-pattern (str "(?s).+?\n\n")))

(defn deliver-events
  [events {:keys [on-next]}]
  (when on-next
    (a/go
      (loop []
        (let [event (a/<! events)]
          (when (not= :done event)
            (on-next event)
            (recur)))))))

(defn- parse-event [raw-event]
  (let [data-idx (string/index-of raw-event "{")
        done-idx (string/index-of raw-event "[DONE]")]
    (if done-idx
      :done
      (-> (subs raw-event data-idx)
          #?(:clj (json/parse-string true)
             :cljs (js->clj :keywordize-keys true))))))

(defn calc-buffer-size
  "Buffer size should be at least equal to max_tokens
  or 16 (the default in openai as of 2023-02-19)
  plus the [DONE] terminator"
  [{:keys [max_tokens]
    :or {max_tokens 16}}]
  (inc max_tokens))

(defn sse-events
  "Returns a core.async channel with events as clojure data structures.
  Inspiration from https://gist.github.com/oliyh/2b9b9107e7e7e12d4a60e79a19d056ee"
  [{:keys [request params]}]
  #?(:clj (let [event-stream ^InputStream (:body #?(:clj (http/request (merge request
                                                                              params
                                                                              {:as :stream}))
                                                    :cljs {}))
                buffer-size (calc-buffer-size params)
                events (a/chan (a/sliding-buffer buffer-size) (map parse-event))]
            (a/thread
              (loop [byte-coll []]
                (let [byte-arr #?(:clj (byte-array (max 1 (.available event-stream)))
                                  :cljs {})
                      bytes-read (.read event-stream byte-arr)]

                  (if (neg? bytes-read)

                    ;; Input stream closed, exiting read-loop
                    (.close event-stream)

                    (let [next-byte-coll (concat byte-coll (seq byte-arr))
                          data #?(:clj (slurp (byte-array next-byte-coll))
                                  :cljs "")]
                      (if-let [es (not-empty (re-seq event-mask data))]
                        (if (every? true? (map #(a/>!! events %) es))
                          (recur (drop (apply + (map #(count (.getBytes %)) es))
                                       next-byte-coll))

                          ;; Output stream closed, exiting read-loop
                          (.close event-stream))

                        (recur next-byte-coll)))))))
            events)))

(defn sse-request
  "Process streamed results.
  If on-next callback provided, then read from channel and call the callback.
  Returns a response with the core.async channel as the body"
  [{:keys [params] :as ctx}]
  (let [events (sse-events ctx)]
    (deliver-events events params)
    {:status 200
     :body events}))

(def perform-sse-capable-request
  {:name ::perform-sse-capable-request
   :leave (fn [{:keys [request params] :as ctx}]
            (assoc ctx :response (if (:stream params)
                                   (sse-request ctx)
                                   #?(:clj (http/request request)
                                      :cljs {}))))})
