(ns base.components.menu-item
  "Menu item component.
  Based on `https://tailgrids.com/components/navbars`

  The parameter map contains:
  * title: what to display to user
  * href: the adress to go to
  * on-click: executed when clicked, overseed href
  * selected: display differently if true
  ")

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn component
  "Build a menu entry"
  [{:keys [title href selected on-click]}]
  [:a.select-none
   (merge {:key (str "menu-" title)
           :class (if selected
                    "cursor-default"
                    "cursor-pointer")}
          (when-not selected
            (if on-click
              {:on-click on-click}
              {:href href})))
   [:div
    {:class (if selected
              "menu-item-selected"
              "menu-item")}
    title]])
