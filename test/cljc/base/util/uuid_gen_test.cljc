(ns base.util.uuid-gen-test
  (:require
   [clojure.test :refer [testing deftest is]]

   [base.util.uuid-gen :as sut]
   ))

(deftest time-based-uuid
  (testing "uuid and chonological orders are the same"
    (let [vecs (map
                (fn [_n]
                  (sut/time-based-uuid))
                (range 10))]
      (is (= (sort (map str vecs))
             (map str vecs)
             )))))

(deftest uguessable
  (testing "check that generates proper uuid"
    (is (every? uuid?
                (repeatedly 10 #(sut/unguessable))))))
