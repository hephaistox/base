(ns base.domain.apps-load-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [base.domain.apps-load :as sut]
   ))

(deftest load-cfg-test
  (testing "Check that app.edn file is well loaded. An exception will raise if the apps.edn is not well loaded"
    (is (vector?
         (sut/load-cfg))))
  (testing "Test that non existing apps file are detected"
    (is (thrown? clojure.lang.ExceptionInfo
                 (sut/load-cfg "non existing file")))))
