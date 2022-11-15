(ns base.domain.apps.schema-test
  (:require
   #?(:clj [clojure.test :refer [deftest is testing]]
      :cljs [cljs.test :refer [deftest is testing]])

   [malli.core :as m]
   [malli.error :as me]

   [base.domain.apps.schema :as sut]
   ))

(def valid-app {:app-name "hello-world"
                :react? true
                :runnable? true})

(deftest app-test
  (testing "Normal app tested "
    (is (nil? (me/humanize (m/validate sut/app valid-app)))))
  (testing "Missing data"
    (is (not (m/validate sut/app (dissoc valid-app :app-name)))))
  (testing "Too much data"
    (is (not (m/validate sut/app (assoc valid-app :unuseful "data"))))))

(deftest apps-test
  (testing "app collection recognized"
    (is (nil? (me/humanize (m/validate sut/apps [valid-app valid-app]))))
    (is (m/validate sut/apps []))))
