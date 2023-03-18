(ns wkok.openai-clojure.core-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [wkok.openai-clojure.core :as core]))

(deftest transform-deprecated-argsn
  (testing "deprecated impl arg gets transformed to new format"

    (is (= {:p {:prompt "hi"}, :o {:impl :azure}}
           (core/transform-deprecated-args? :azure {:prompt "hi"})))

    (is (= {:p {:prompt "hi"}, :o {:impl :openai}}
           (core/transform-deprecated-args? :openai {:prompt "hi"})))

    (is (= nil
           (core/transform-deprecated-args? {:prompt "hi"} nil)))))
