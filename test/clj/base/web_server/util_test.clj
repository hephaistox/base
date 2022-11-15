(ns base.web-server.util-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [base.web-server.util :as sut]
   ))

(deftest mapToJSmap
  (testing "Empty map"
    (is (= "{}"
           (sut/mapToJSmap {}))))
  (testing "String map"
    (is (= "{\"foo\": \"bar\"}"
           (sut/mapToJSmap {:foo "bar"}))))
  (testing "keyword with minus"
    (is (= "{\"is_foo\": \"bar\"}"
           (sut/mapToJSmap {:is-foo "bar"}))))
  (testing "Integer value"
    (is (= "{\"foo\": 1}"
           (sut/mapToJSmap {:foo 1}))))
  (testing "keyword value"
    (is (= "{\"foo_from\": \"bar_to\"}"
           (sut/mapToJSmap {:foo-from :bar-to}))))
  (testing "Multiple values"
    (is (= "{\"foo_from\": \"bar_to\",\n\"foo\": 1,\n\"bar\": \"foo\"}"
           (sut/mapToJSmap {:foo-from :bar-to
                            :foo 1
                            :bar "foo"})))))
