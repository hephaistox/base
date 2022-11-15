(ns base.devcards.menu
  (:require
   [base.components.menu :as sut]

   [reagent.core :as r]
   [devcards.core :as dc :refer [defcard]]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defcard menu
  (r/as-element
   [:div
    [:h1.text-3xl
     "Show normal menu, Menu2 is forced to be selected"]
    [sut/component {:items [{:title "Menu1"
                             :href "/admin/devcards#!/base.devcards.menu"}
                            {:title "Menu2"
                             :href "/admin/devcards?menu2=true#!/base.devcards.menu"
                             :selected true}
                            {:title "Menu3"
                             :href "/admin/devcards?menu3=true#!/base.devcards.menu"}
                            {:title "Menu4"
                             :href "/admin/devcards?menu4=true#!/base.devcards.menu"}]}]
    [:p.text-sm "Should be displayed on the right. On small screen, a burger menu, and drop down list, selected item is underlined"]
    [:h1.text-3xl "Menu with auto select"]
    [sut/component {:items [{:title "Menu1"
                             :href "/admin/devcards#!/base.devcards.menu"}
                            {:title "Menu2"
                             :href "/admin/devcards?menu2=true#!/base.devcards.menu"
                             :selected true}
                            {:title "Menu3"
                             :href "/admin/devcards?menu3=true#!/base.devcards.menu"}
                            {:title "Menu4"
                             :href "/admin/devcards?menu4=true#!/base.devcards.menu"}]
                    :path (.. js/window -location -href)
                    :burger-position :left}]
    [:p.text-sm "If you select a menu, the page should reload and show the menu as selected. The selected parameter is set to menu2, but should be automatically discarded by the component as the `path` parameter has higher priority. As set, this `path` parameter trigger auto selection of the menu"]
    [:h1.text-3xl "Menu forced to be in burger mode"]
    [sut/component {:items [{:title "Menu1"
                             :href "/admin/devcards#!/base.devcards.menu"}
                            {:title "Menu2"
                             :href "/admin/devcards?menu2=true#!/base.devcards.menu"
                             :selected true}
                            {:title "Menu3"
                             :href "/admin/devcards?menu3=true#!/base.devcards.menu"}
                            {:title "Menu4"
                             :href "/admin/devcards?menu4=true#!/base.devcards.menu"}]
                    :path (.. js/window -location -href)
                    :force-burger? true
                    :burger-position :left}]]))
