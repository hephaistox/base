(ns base.core-test
  "Used to init test environment"
  (:require
   [clojure.test :refer [deftest is testing]]
   [mount.core :as mount]
   ))

;; Should be set before any use of mount function, as all clj defmount calls are made with @
(mount/in-cljc-mode)

(deftest dumb-test
  (testing "Check it"
    (is (= 3
           (+ 2 1)))))
