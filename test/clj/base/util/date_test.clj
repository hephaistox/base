(ns base.util.date-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [base.util.date :as sut]
   ))

(deftest this-year
  (testing "Check now"
    (is (>= (Integer/parseInt (sut/this-year))
            2022))
    (is (>= (Integer/parseInt (sut/this-year (java.util.Date.)))
            2022))))
