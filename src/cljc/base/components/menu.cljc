(ns base.components.menu
  "Display a menu component"
  (:require
   [clojure.string]

   [base.components.menu-item :as menu-item]
   [base.components.svg-icons :as svg-icons]
   [base.util.url :as url]))

(defn- horizontal-version
  "Shown as horizontal following buttons"
  [{:keys [items force-burger?]}]
  (vec (concat
        [:div.flex.flex-col.xl:flex-row.menu.hidden
         (when-not force-burger?
           {:class "xl:block"})]
        items)))

(defn- burger-version
  "Show the minified - burger version"
  [{:keys [items burger-position force-burger?]}]
  [:div.block.menu-burger-block {:class (when-not force-burger?
                                          "xl:hidden")}
   [:div.absolute {:class (if (= :left burger-position)
                            "menu-burger-block-left"
                            "menu-burger-block-right")}
    [:div.overflow-hidden.hover:h-fit.relative.menu-burger-block-default-block-height
     [:div.flex.flex-col.absolute
      {:class (if (= :left burger-position)
                "menu-burger-in-block-left"
                "menu-burger-in-block-right")}
      [:div.cursor-pointer
       [svg-icons/icon {:path-kw :svg/burger}]]
      #_[:div.cursor-pointer.mt-20
         [svg-icons/icon {:path-kw :svg/x-mark}]]]
     (vec
      (concat
       [:div.h-fit.flex.flex-col.mt-10.menu-burger-items-box]
       items))]]])

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn component
  "An horizontal menu adapting to small screen to become vertical
  The parameter is a map with the following keys:
  * `items` is a vector a menu-item, See the `base.components.menu-item` for more options
  * `path` if set the menu is auto marked as selecting the matching menu (the one which href is contained in this parameter)
  * `force-burger?` if the menu should be minified as a burger, whatever the size of the screen is
  * `burger-position` where the minified version (burger) is shown on the remaining space, `:left` or `:right`
  "
  [{:keys [items path]
    :as params}]
  (let [auto-select? (not (nil? path))
        items (vec
               (for [{:keys [href]
                      :as item} items]
                 (let [menu-match? (when (and (string? path)
                                              (string? href))
                                     (let [uri (url/get-uri path)]
                                       (= uri
                                          href)))]
                   [menu-item/component (merge (assoc item
                                                      :path path
                                                      :auto-select? auto-select?)
                                               (when auto-select?
                                                 {:selected menu-match?}))])))
        params (assoc params :items items)]
    [:div.w-full.z-10
     [horizontal-version params]
     [burger-version params]]))
