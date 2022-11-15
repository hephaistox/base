(ns base.components.header
  "Simple header")

(defn component
  "Display a header component, as of https://tailwindui.com/components/marketing/elements/headers"
  [content]
  [:header.header-inner
   [:div.flex.relative.items-center.justify-between
    {:class "-mx-4"}
    content]])
