(ns base.components.alert
  "An alert component to present a message and its title,
  based on https://tailwindui.com/components/application-ui/feedback/alerts"
  (:require
   [base.components.svg-icons :as svg-icons]))

(def alert-types
  [:warning :assert :error])

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn component
  "Component to show an alert
  * title: what to display as a first highlighted line
  * message: what to display as a second line
  * type: one of the value :warning, :assert or :error (default to :error)
  "
  [{:keys [title message type]}]
  (let [[color icon] (case type
                       :warning ["warning" :svg/exclamation]
                       :assert ["assert" :svg/checked]
                       ;;:error
                       ["error" :svg/x-circle])]
    [:div.flex
     {:class (str "alert-" color)}
     [:div.flex-shrink-0
      (svg-icons/icon {:path-kw icon
                       :class (str "alert-icon-" color)})]
     [:div.ml-3
      (when title
        [:h3 {:class (str "alert-title-" color)}
         title])
      (when message
        [:p {:class (str "alert-message-"
                         color
                         (when title
                           " mt-2"))}
         message])]
     [:div.ml-auto.pl-3
      [:div.cursor-pointer {:class "-mx-1.5 -my-1.5"}
       [:div
        {:class (str "alert-close-" color)}
        (svg-icons/icon {:width 20
                         :class ""
                         :path-kw :svg/dismiss-button})]]]]))

;; alert-warning alert-icon-warning alert-title-warning alert-message-warning alert-close-warning
;; alert-error alert-icon-error alert-title-error alert-message-error alert-close-error
;; alert-assert alert-icon-assert alert-title-assert alert-message-assert alert-close-assert
