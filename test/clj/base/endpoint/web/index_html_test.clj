(ns base.endpoint.web.index-html-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [base.endpoint.web.index-html :as sut]
   ))

;;NOTE The antiforgery is tested in the mocked http server
(deftest index-html
  (testing "index html not raising exception"
    (is (sut/build {} [:foo :bar]))))
