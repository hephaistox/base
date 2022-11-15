(ns base.endpoint.realtime.message-handler
  "Basic message handler, the `apps` deriving from base should
add some other defmethod, for all other messages"
  (:require
   [base.log :as log]
   ))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id _?data _event]}]
  (log/trace "Realtime query: " id)
  (-event-msg-handler ev-msg) ; Handle event-msgs on a thread pool
  )

;; Default/fallback case (no other matching handler)
(defmethod -event-msg-handler
  :default
  [{:keys [event ring-req ?reply-fn _send-fn] :as _params}]
  (let [session (:session ring-req)
        uid     (:uid     session)]
    (log/trace "Default faultback realtime: uid=" uid ", session=" session)
    (when ?reply-fn
      (?reply-fn {:umatched-event-as-echoed-from-server event}))))

(defmethod -event-msg-handler
  :yop/test
  [{:keys []}]
  (log/trace "Test successful"))
