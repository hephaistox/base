(ns base.endpoint.realtime.routes
  "Routes for realtime discussion between server and frontend"
  (:require
   [mount.core :refer [defstate]]
   [ring.middleware.anti-forgery]
   [ring.middleware.keyword-params]
   [ring.middleware.params]

   [base.endpoint.session]
   [base.endpoint.realtime.core :as rt]
   [base.endpoint.web.middleware :as web-middleware]
   [base.log :as log]
   [base.server.cst :as cst]
   ))

(defn routes*
  ([]
   (routes* (rt/ajax-get-or-ws-handshake)
            (rt/ajax-post)))
  ([get-handler put-handler]
   (let [route [cst/realtime-uri ;; The channel url is used for realtime
                {:summary "Realtime channel"
                 :middleware [
                              base.endpoint.session/wrap-session                 ;; Manage user session in cookies
                              ring.middleware.anti-forgery/wrap-anti-forgery     ;; Prevents CSRF attack
                              ring.middleware.params/wrap-params                 ;; Add query-params, form-params and params to the request map
                              ring.middleware.keyword-params/wrap-keyword-params ;; Translate string keys in the :params to keywords
                              web-middleware/wrap-copy-params                    ;; For dev only, copy query in response
                              ]
                 :get {:handler get-handler}
                 :put {:handler put-handler}}]]
     route)))

(defstate routes
  :start (do
           (log/info "Starting realtime routes")
           (let [res (routes*)]
             (log/trace "Realtime routes are started")
             res)))
