(ns base.components.footer
  (:require
   [base.components.svg-icons :as svg-icons]
   [base.components.version :as version]
   ))

(defn- footer-link
  [{:keys [title items] :as _footer-category}
   class]
  [:div {:class class}
   [:h3.footer-category title]
   (vec
    (concat [:ul.footer-category-title {:role "list" }]
            (for [{:keys [text href]} items]
              [:li.footer-link [:a {:href href} text]])))])

(defn component
  "Display a footer,
  Is managing the different size of screens
  * `compagny-name` used as a alt of the image
  * `badge` is an url of the badge to display on the left
  * `quotation` text to show under the badge
  * `title` is the far bottom of the footer
  * `footer-lists` vector of maps with
     * `title` the title of a section,
     * `items` a vector of links, each is a map of
        * `href` for target adress,
        * and `text` for what to display
  * `social-networks` is a vector of map
      *  `href` for target adress
      * `sr-name` the alt name for the link
      * `icon` is the icon of `svg_icon` to use
  * `version` is the string to show the version
  "
  []
  (fn [{:keys [social-networks footer-lists title quotation badge compagny-name version]}]
    [:footer.footer {:aria-labelledby "footer-heading"}
     [:div.footer-inner
      [:div.footer-columns-gap.xl:grid.xl:grid-cols-3
       [:div.footer-identity-column
        [:img.footer-badge {:src badge, :alt compagny-name}]
        [:p.footer-quotation quotation]
        (vec
         (concat
          [:div.flex]
          (for [{:keys [href sr-name icon]} social-networks]
            [:a.footer-icon {:href href}
             [:span.sr-only sr-name]
             [svg-icons/icon {:width 30
                              :path-kw icon}]])))]
       (let [[footer1 footer2 footer3 footer4] footer-lists]
         [:div.grid.grid-cols-2.gap-8.xl:col-span-2.footer-link-column
          [:div.md:grid.md:grid-cols-2.footer-link-column-gap
           [footer-link footer1]
           [footer-link footer2 "footer-link-2nd-and-4th"]]
          [:div.md:grid.md:grid-cols-2.footer-link-column-gap
           [footer-link footer3]
           [footer-link footer4 "footer-link-2nd-and-4th"]]])]
      [:div.footer-compagny-block.relative
       [version/component version]
       [:p.footer-compagny-text title]
       ]]]))

