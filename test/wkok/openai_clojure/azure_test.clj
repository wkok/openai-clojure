(ns wkok.openai-clojure.azure-test
  (:require
   [clojure.string :as string]
   [clojure.test :refer [deftest is testing]]
   [martian.core :as martian]
   [wkok.openai-clojure.azure :as azure]))

(deftest request-format
  (testing "request has the correct format"
    (let [spec-version (-> (azure/load-openai-spec) :info :version)
          model "text-davinci-003"
          params {:model model
                  :prompt "Say this is a test"
                  :max_tokens 7
                  :temperature 0}
          request (martian/request-for @azure/m :create-completion
                                       (azure/patch-params params))]

      (testing "api-version matches spec"
        (is (= spec-version
               (-> request :query-params :api-version))))

      (testing "contains api-key header"
        (is (contains? (:headers request) "api-key")))

      (testing "deployment-id in url"
        (is (string/includes? (:url request) model)))

      (testing "params patched correctly"
        (is (= {:api-version spec-version
                :deployment-id (:model params)
                :martian.core/body (dissoc params :model)}
               (azure/patch-params params)))))))

(deftest add-headers-init
  (let [add-headers-fn (-> azure/add-authentication-header :enter)]
    (testing "atoms get initialized correctly"

      (is (= {"api-key" "my-secret-key"}
             (-> (add-headers-fn {:params {:wkok.openai-clojure.core/options {:api-key "my-secret-key"}}})
                 :request
                 :headers))))))
