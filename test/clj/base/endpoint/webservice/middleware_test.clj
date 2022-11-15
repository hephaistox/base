(ns base.endpoint.webservice.middleware-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [base.endpoint.webservice.middleware :as sut]
   ))

(defn handler-test
  [request]
  (assoc request
         :foo :bar))

(deftest middleware-test
  (let [app (sut/wrap-transit handler-test)]
    (testing "wrap is still a function"
      (is (fn? app)))
    (testing "wrap is adding the header"
      (is (= {:bar2 :foo2
              :headers {"Accept" "application/transit+json"}
              :foo :bar}
             (app {:bar2 :foo2}))))))
