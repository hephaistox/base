(ns base.webui.routes-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [reitit.core :as reitit]

   [base.webui.routes :as sut]
   ))

(def routes-stub
  (reitit/router
   ["/#/"
    [""      :home-panel]
    ["about" :about-panel]]))

(deftest parse
  (testing "Test basic uri"
    (is (= :about-panel
           (sut/parse routes-stub "/#/about")))
    (is (nil?
         (sut/parse routes-stub "/#")))
    (is (= :home-panel
           (sut/parse routes-stub "/#/")))
    (is (nil?
           (sut/parse routes-stub "/#/not-existing-one")))))

(deftest url-for
  (testing "Uri are found by their name"
    (is (= "/#/about"
           (:path (sut/url-for routes-stub :about-panel))))
    (is (= "/#/"
           (:path (sut/url-for routes-stub :home-panel))))
    (is (nil? (:path (sut/url-for routes-stub :not-existing))))))
