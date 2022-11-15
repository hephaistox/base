(ns base.blob.load-test
  (:require
   [clojure.test :refer [deftest testing is]]

   [base.blob.load :as sut]
   ))

(def stub [{:name "O1"
            :machine "m1"
            :duration 5}
           {:name "O2"
            :machine "m2"
            :duration 2}
           {:name "J1"
            :ops ["O1" "O2"]}
           {:name "m1"}])

(def stub-slurp
  (constantly
   (with-out-str
     (pr stub))))

(deftest load-edn
  (testing "test file"
    (is (= stub
           (with-redefs [slurp stub-slurp]
             (sut/load-edn "js/ft11.edn"))))))
