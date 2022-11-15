(ns base.endpoint.web.dev-route
  "Defines the specific dev routes.
  That routes should only be used in the dev environment"
  (:require
   [hiccup.core :as hiccup]
   [ring.util.http-response :as response]

   [base.endpoint.web.admin.index :as admin-index]))

(defn route
  [build build-without-structure]
  ["admin"
   {:summary "Admin subdir"}
   [""
    {:get (fn [request]
            (let [admin-content (admin-index/index request)]
              (-> request
                  (build admin-content)
                  hiccup/html
                  response/ok)))}]
   ["/throw-exception"
    {:summary "Raise intentionally an exception"
     :get (fn [_request] (throw (ex-info "This exception is raised intentionally" {:to "check"
                                                                                   :what "happens"})))}]
   ["/devcards"
    {:summary "Show devcards"
     :get (fn [request]
            (-> request
                (build-without-structure [:div ""
                                          [:script {:type "text/javascript"
                                                    :src "/js/compiled/share.js"}]
                                          [:script {:type "text/javascript"
                                                    :src "/js/compiled/devcards.js"}]])
                hiccup/html
                response/ok))}]
   ["/blank" {:summary "Show the page structure only"
              :get (fn [request] (-> request
                                     (build "")
                                     hiccup/html
                                     response/ok))}]])
