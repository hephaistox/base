(ns base.components.button-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [base.components.button :as sut]))

(deftest component
  (testing "Url set"
    (is (sut/component {:url "/"})))
  (testing "on click set"
    (is (sut/component {:on-click #(fn [_] 1)})))
  (testing "Url and on-click no set trigger an exception"
    (is (sut/component {}))))
