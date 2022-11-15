(ns base.util.url-test
  (:require
   [base.util.url :as sut]
   [clojure.test :refer [deftest is testing]]))

(deftest extract-path
  (testing "extract no path"
    (is (= ["example.com/" nil nil "example.com/" nil nil]
           (sut/extract-path "example.com/")))
    (is (= ["http://example.com/" "http" "example.com" "/" nil nil]
           (sut/extract-path "http://example.com/")))
    (is (= ["https://example.com/" "https" "example.com" "/" nil nil]
           (sut/extract-path "https://example.com/")))
    (is (= ["https://www.example.com/" "https" "www.example.com" "/" nil nil]
           (sut/extract-path "https://www.example.com/")))
    (is (= ["example.com" nil nil "example.com" nil nil]
           (sut/extract-path "example.com"))))
  (testing "extract static path"
    (is (= ["http://example.com/foo?k=1" "http" "example.com" "/foo" "k=1" nil]
           (sut/extract-path "http://example.com/foo?k=1"))))
  (testing "extract static path"
    (is (= ["http://example.com/foo?k=1&p=2" "http" "example.com" "/foo" "k=1&p=2" nil]
           (sut/extract-path "http://example.com/foo?k=1&p=2"))))
  (testing "extract spa path"
    (is (= ["http://example.com/#foo?foo=bar&bar=foo" "http" "example.com" "/" nil "foo?foo=bar&bar=foo"]
           (sut/extract-path "http://example.com/#foo?foo=bar&bar=foo"))))
  (testing "extract spa path"
    (is (= ["http://example.com?foo=bar&bar=foo#barfoo" "http" "example.com" "" "foo=bar&bar=foo" "barfoo"]
           (sut/extract-path "http://example.com?foo=bar&bar=foo#barfoo")))))

(deftest get-protocol
  (testing "extract protocol"
    (is (= "http"
           (sut/get-protocol "http://example.com/foo?k=1")))))

(deftest get-path
  (testing "extract path"
    (is (= "example.com"
           (sut/get-path "http://example.com/foo?k=1")))))

(deftest get-static-page
  (testing "extract static page"
    (is (= "/foo"
           (sut/get-static-page "http://example.com/foo?k=1")))))

(deftest get-parameters
  (testing "extract parameters"
    (is (= "k=1&l=1"
           (sut/get-parameters "http://example.com/foo?k=1&l=1")))))

(deftest get-spa-page
  (testing "extract spa page"
    (is (= "abcabc"
           (sut/get-spa-page "http://example.com/foo?k=1&l=1#abcabc")))))

(deftest get-uri
  (testing "extract uri"
    (is (= "/foo?k=1&l=1#abcabc"
           (sut/get-uri "http://example.com/foo?k=1&l=1#abcabc")))))
