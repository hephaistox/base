(ns base.endpoint.web.index-html
  (:require
   [ring.middleware.anti-forgery :as anti-forgery]

   [base.web-server.util :as ws-util]))

(defn build
  "Build a webpage header"
  [{:keys [app-name language envVars body-append]} body]
  (let [anti-forgery-token  (force anti-forgery/*anti-forgery-token*)]
    [:html
     {:lang language}
     [:head
      [:meta {:charset "utf-8"}]
      [:meta
       {:content "width=device-width,initial-scale=1", :name "viewport"}]
      [:link {:type "text/css", :rel "stylesheet", :href "/css/main.css"}]
      [:script (format "var _envVars =%s"
                       (ws-util/mapToJSmap envVars))]
      [:script {:src "https://cdn.jsdelivr.net/npm/tw-elements/dist/js/index.min.js"}]
      [:title app-name]]
     (vec
      (concat
       [:body [:div.hidden {:name  "__anti-forgery-token"
                            :id    "__anti-forgery-token"
                            :anti-forgery-token anti-forgery-token}]
        body]
       body-append))]))
