(ns base.domain.apps-test
  (:require
   #?(:clj [clojure.test :refer [deftest is testing]]
      :cljs [cljs.test :refer [deftest is testing]])

   [base.domain.apps :as sut]
   ))

(def fake-apps [{:app-name "hello-world"
                 :react? true
                 :testable? true
                 :runnable? true}
                {:app-name "app"
                 :react? true
                 :testable? true
                 :runnable? true}
                {:app-name "base"
                 :react? true
                 :testable? true
                 :runnable? true}
                {:app-name "build"
                 :react? true
                 :testable? true
                 :runnable? false}
                {:app-name "time-lib"
                 :react? true
                 :testable? true
                 :runnable? false}])

(deftest validate-test
  (testing "Try a good app"
    (is
     (sut/validate fake-apps)))
  (testing "Try a wrong one"
    (is
     (thrown?
      #?(:clj clojure.lang.ExceptionInfo
         :cljs ExceptionInfo.)
      (sut/validate (concat {:foo :bar} fake-apps))))))

(deftest apps-names-test
  (is (= ["hello-world" "app" "base" "build" "time-lib"]
         (sut/apps-names fake-apps)))
  (is (empty? (sut/apps-names {}))))

(deftest app-get-by-name-test
  (is (sut/app-get-by-name fake-apps "hello-world"))
  (is (sut/app-get-by-name fake-apps "time-lib"))
  (is (sut/app-get-by-name fake-apps "build"))
  (is (not (sut/app-get-by-name fake-apps nil)))
  (is (not (sut/app-get-by-name fake-apps "foo"))))
