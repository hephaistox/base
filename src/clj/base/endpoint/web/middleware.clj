(ns base.endpoint.web.middleware
  "Middlewares for web server"
  (:require
   [ring.util.http-response :as response]
   ))

(defn wrap-deny-frame
  "Disallow iFrame"
  [handler]
  (fn [request]
    (let [response (handler request)]
      (response/header response
                       "X-Frame-Options"
                       "DENY"))))

(defn wrap-copy-params
  "Copy in :request-copied key the request
  For dev only"
  ([handler kw]
   (fn [request]
     (let [response (handler request)]
       (assoc response
              kw request))))
  ([handler]
   (wrap-copy-params handler :request-copied)))
