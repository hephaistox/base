(ns base.components.file-loader-test
  (:require
   [clojure.test :refer [deftest is testing]]

   [base.components.file-loader :as sut]
   ))

(deftest component
  (testing ""
      (is (= [:div "Error in the component parameters"]
             (sut/component {})
             ))))
