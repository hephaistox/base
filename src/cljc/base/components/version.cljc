(ns base.components.version
  (:require
   [base.components.svg-icons :as svg-icons]))

(defn component
  "Show versions"
  [version]
  [:div.inline-block.absolute.right-0
   [svg-icons/icon {:width 30
                    :class "mx-auto svg_icon_primary"
                    :path-kw :svg/ant}]
   [:span.text-xs version]])
