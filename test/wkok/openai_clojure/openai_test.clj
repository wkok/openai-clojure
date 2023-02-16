(ns wkok.openai-clojure.openai-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer [deftest is testing]]
   [martian.core :as martian]
   [wkok.openai-clojure.openai :as openai]))

(defn- find-multipart
  [request name]
  (->> request
       :multipart
       (some #(when (= name (:name %)) %))))

(deftest multipart-request-format
  (testing "request has the correct format"
    (let [request (martian/request-for @openai/m :create-image-edit
                                       {:image (io/file "path/to/otter.png")
                                        :mask (io/file "path/to/mask.png")
                                        :prompt "A cute baby sea otter wearing a beret"
                                        :n 2
                                        :size "1024x1024"})]

      (testing "contains Authorization header"
        (is (contains? (:headers request) "Authorization")))

      (testing "multipart image file set correctly"
        (let [otter (find-multipart request "image")]
          (is (instance? java.io.File (:content otter)))))

      (testing "multipart prompt set correctly"
        (let [prompt (find-multipart request "prompt")]
          (is (= "A cute baby sea otter wearing a beret"
                 (:content prompt))))))))
