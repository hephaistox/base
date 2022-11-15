(ns base.devcards.version
  (:require
   [reagent.core :as r]

   [devcards.core :as dc :refer [defcard]]
   [base.components.version :as sut]
   ))

(defcard version
  (r/as-element
   [:div.flex.flex-col
    [:h1.text-3xl "Show the version"]
    [sut/component "1234"]]))
