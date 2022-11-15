(ns base.components.button
  "Clickable buttons"
  )

;; https://tailgrids.com/components/buttons >> Outline Button Style 1
 #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
 (defn component
  "A simple clickable button"
   [{:keys [text on-click url]
    :as params}]
  [:button.select-none.inline-flex.items-center.justify-center.text-center.button
   (cond
     on-click {:on-click on-click}
     url {:href url}
     :else (throw (ex-info "Button should have url or on-click set"
                           {:params params})))
   (or text
       "OK")])
