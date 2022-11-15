(ns base.devcards.header
  (:require
   [reagent.core :as r]

   [devcards.core :as dc :refer [defcard]]
   [base.components.header :as sut]
   [base.components.menu :as menu]))

(defn menu
  []
  [menu/component {:items [{:title "Menu"
                            :href "/#/"}
                           {:title "Menu1"
                            :href "/#/"}
                           {:title "Menu2"
                            :href "/#/"}]}])

(defcard header
  (r/as-element
   [:div
    [:h3.text-3xl "Show header"]
    [sut/component (menu)]]))
