(ns base.devcards.menu-item
  (:require
   [base.components.menu-item :as sut]

   [reagent.core :as r]
   [devcards.core :as dc :refer [defcard]]
   ))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defcard menu-item
  (r/as-element
   [:div
    [sut/component {:title "Normal menu"
                    :href "/coucou"}]

    [sut/component {:title "Selected menu"
                    :selected true
                    :href "/coucou"}]

    [sut/component {:title "Check click action works"
                    :on-click #(js/alert "Hey, click action works!")}]
    ]))
