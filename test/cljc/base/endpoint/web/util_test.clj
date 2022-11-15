(ns base.endpoint.web.util-test
  (:require
   [clojure.test :refer [deftest testing is]]

   [base.endpoint.web.util :as sut]
   ))

(deftest parse-cookie
  (testing "Cookie parse 1 and 2 semi column per line"
    (is (= {"ring-session" "496456e5-2860-4a50-9702-51a7cd13d017", "Path" "/", ["HttpOnly"] nil}
           (sut/parse-cookie
            "ring-session=496456e5-2860-4a50-9702-51a7cd13d017;Path=/;HttpOnly"))))
  (testing "Cookie parse 1 and 2 semi column per line"
    (is (= {"a" ["b" "c"]}
           (sut/parse-cookie
            "a=b=c"))))
  (testing "Cookie parse nil"
    (is (nil? (sut/parse-cookie "")))
    (is (nil? (sut/parse-cookie nil)))))
