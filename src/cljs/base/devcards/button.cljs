(ns base.devcards.button
  (:require [reagent.core :as r]

            [devcards.core :as dc :refer [defcard]]
            [base.components.button :as sut]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defcard button
  (r/as-element
   [:div
    [:h1.text-3xl "Show alerts in different situations"]

    [:h2.text-2xl "Normal size, type :error"]
    [:h3 "Default url"]
    [sut/component {:url "/"}]
    [:h3 "Default text"]
    [sut/component {:url"/"}]
    [:h3 "Normal"]
    [sut/component {:text "Button"
                    :url "/"}]
    [:h3 "Many buttons to see in small screens"]
    [:div.flex.flex-row
     [sut/component {:text "Button"
                     :url"/"}]
     [sut/component {:text "Button"
                     :url"/"}]
     [sut/component {:text "Button"
                     :url"/"}]]
    ]))
