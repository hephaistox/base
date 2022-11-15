(ns base.util.conf-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [mount.core :as mount]

   [base.util.conf :as sut]))

(deftest check-conf-working
  (let [_ (mount/start-with-states
           {#'base.util.conf/conf-state {:start sut/start-conf
                                         :stop sut/stop-conf}})]
    (testing "Check test environment is dev"
      (is (= :dev
             (sut/read-param [:env]))))

    (mount/stop)))
