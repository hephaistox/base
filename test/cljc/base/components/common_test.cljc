(ns base.components.common-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [base.components.common :as sut]))


(deftest combine-classes
  (testing "Join classes is separated with strings"
    (is (= "a b"
           (sut/combine-classes "a" "b")))
    )
  (testing "Only one class works"
    (is (= "a"
           (sut/combine-classes "a"))))
  (testing "No class is ok"
    (is (= ""
           (sut/combine-classes)))))

(deftest combine-classes-with-custom
  (testing "Custom classes are higher priority"
    (is (= "d e a b c"
           (sut/combine-classes-with-custom "a b c"
                                            :merge
                                            "d" "e"))
        )
    )
  (testing "Custom only don't select default at all"
    (is (= "a b c"
           (sut/combine-classes-with-custom "a b c"
                                            :custom-only
                                            "d e"))))
  (testing "Default only don't select default at all"
    (is (= "d e"
           (sut/combine-classes-with-custom "a b c"
                                            :default-only
                                            "d" "e")))))
