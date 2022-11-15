(ns base.log.core-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [base.log.core :as sut]
   ))

(deftest build-logfilename-test
  (testing "Create a filename"
    (is (string?
         (sut/build-logfilename "foo")))))
