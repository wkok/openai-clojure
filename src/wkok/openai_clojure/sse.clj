(ns ^:no-doc wkok.openai-clojure.sse
  (:require
   [hato.client :as http]
   [hato.middleware :as hm]
   [clojure.core.async :as a]
   [clojure.string :as string]
   [cheshire.core :as json]
   [clojure.core.async.impl.protocols :as impl])
  (:import (java.io InputStream)
           (clojure.lang Counted)
           (java.util    LinkedList)))

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
          (json/parse-string true)))))

(deftype InfiniteBuffer [^LinkedList buf]
  impl/UnblockingBuffer
  impl/Buffer
  (full? [_this]
    false)
  (remove! [_this]
    (.removeLast buf))
  (add!* [this itm]
    (.addFirst buf itm)
    this)
  (close-buf! [_this])
  Counted
  (count [_this]
    (.size buf)))

(defn infinite-buffer []
  (InfiniteBuffer. (LinkedList.)))

(defn calc-buffer-size
  "- Use stream_buffer_len if provided.
   - Otherwise, buffer size should be at least equal to max_tokens
     plus the [DONE] terminator if it is provided.
   - Else fallbacks on ##Inf and use an infinite-buffer instead"
  [{:keys [stream_buffer_len max_tokens]}]
  (or stream_buffer_len
      (when max_tokens (inc max_tokens))
      ##Inf))

(defn make-buffer [params]
  (let [size (calc-buffer-size params)]
    (if (= size ##Inf)
      (infinite-buffer)
      (a/sliding-buffer size))))

(defn sse-events
  "Returns a core.async channel with events as clojure data structures.
  Inspiration from https://gist.github.com/oliyh/2b9b9107e7e7e12d4a60e79a19d056ee"
  [{:keys [request params]}]
  (let [event-stream ^InputStream (:body (http/request (merge request
                                                              params
                                                              {:as :stream})))
        events (a/chan (make-buffer params) (map parse-event))]
    (a/thread
      (loop [byte-coll []]
        (let [byte-arr (byte-array (max 1 (.available event-stream)))
              bytes-read (.read event-stream byte-arr)]

          (if (neg? bytes-read)

            ;; Input stream closed, exiting read-loop
            (.close event-stream)

            (let [next-byte-coll (concat byte-coll (seq byte-arr))
                  data (slurp (byte-array next-byte-coll))]
              (if-let [es (not-empty (re-seq event-mask data))]
                (if (every? true? (map #(a/>!! events %) es))
                  (recur (drop (apply + (map #(count (.getBytes %)) es))
                               next-byte-coll))

                  ;; Output stream closed, exiting read-loop
                  (.close event-stream))

                (recur next-byte-coll)))))))
    events))

(defn sse-request
  "Process streamed results.
  If on-next callback provided, then read from channel and call the callback.
  Returns a response with the core.async channel as the body"
  [{:keys [params] :as ctx}]
  (let [events (sse-events ctx)]
    (deliver-events events params)
    {:status 200
     :body events}))

(defn wrap-trace
  "Middleware that allows the user to supply a trace function that
  will receive the raw request & response as arguments.
  See: https://github.com/gnarroway/hato?tab=readme-ov-file#custom-middleware"
  [trace]
  (fn [client]
    (fn
      ([req]
       (let [resp (client req)]
         (trace req resp)
         resp))
      ([req respond raise]
       (client req
               #(respond (do (trace req %)
                             %))
               raise)))))

(defn middleware
  "The default list of middleware hato uses for wrapping requests but
  with added wrap-trace in the correct position to allow tracing of error messages."
  [trace]
  [hm/wrap-request-timing

   hm/wrap-query-params
   hm/wrap-basic-auth
   hm/wrap-oauth
   hm/wrap-user-info
   hm/wrap-url

   hm/wrap-decompression
   hm/wrap-output-coercion

   (wrap-trace trace)

   hm/wrap-exceptions
   hm/wrap-accept
   hm/wrap-accept-encoding
   hm/wrap-multipart

   hm/wrap-content-type
   hm/wrap-form-params
   hm/wrap-nested-params
   hm/wrap-method])

(def perform-sse-capable-request
  {:name  ::perform-sse-capable-request
   :leave (fn [{:keys [request params] :as ctx}]
            (let [{{trace :trace} :wkok.openai-clojure.core/options} params]
              (assoc ctx :response (if (:stream params)
                                     (sse-request ctx)
                                     (http/request
                                       (if trace
                                         (assoc request :middleware (middleware trace))
                                         request))))))})
