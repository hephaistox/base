(ns base.endpoint.webservice.middleware
  "Webservice middleware"
  (:require
   [ring.util.http-response :as response]
   ))

(defn wrap-transit
  "Transit json acceptance"
  [handler]
  (fn [request]
    (let [response (handler request)]
      (response/header response
                       "Accept" "application/transit+json"))))
