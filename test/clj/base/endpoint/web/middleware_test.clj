(ns base.endpoint.web.middleware-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [base.endpoint.web.middleware :as sut]
   ))

(defn handler-test
  [request]
  (assoc request
         :foo :bar))

(deftest middleware
  (let [app (sut/wrap-deny-frame handler-test)]
    (testing "wrap is still a function"
      (is (fn? app)))
    (testing "wrap is still a function"
      (is (= {:bar2 :foo2
              :headers {"X-Frame-Options" "DENY"}
              :foo :bar}
             (app {:bar2 :foo2}))))))

(deftest wrap-copy-params
  (let [app (sut/wrap-copy-params handler-test)]
    (testing "wrap is still a function"
      (is (fn? app)))
    (testing "wrap is still a function"
      (is (= {:bar2 :foo2
              :request-copied {:bar2 :foo2}
              :foo :bar}
             (app {:bar2 :foo2}))))))
