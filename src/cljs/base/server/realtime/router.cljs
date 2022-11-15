(ns base.server.realtime.router
  (:require
   [taoensso.sente :as sente]

   [base.log :as log]
   [base.server.realtime.message-handler :as msg-handler]
   [base.server.realtime.core :as realtime]
   ))

(defonce router_ (atom nil))

(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))

(defn start-router! []
  (stop-router!)
  (log/trace "Starting realtime router")
  (reset! router_
    (sente/start-client-chsk-router!
      realtime/ch-chsk msg-handler/event-msg-handler)))
