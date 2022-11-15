(ns base.server.realtime.message-handler
  "Message handlers for realtime"
  (:require
   [base.log :as log]
   [base.server.realtime.core :as rt]
   ))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  (fn [{:keys [id ?data] :as ev-msg}]
    (log/trace ev-msg (first ?data))
    (if (= :chsk/recv id)
      [id (first ?data)]
      [id nil])))

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [_id _?data _event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  :default ; Default/fallback case (no other matching handler)
  [{:as _ev-msg :keys [event]}]
  (log/trace "Unhandled event: %s" event))

(defmethod -event-msg-handler
  [:chsk/state nil]
  [{:as _ev-msg :keys [?data]}]
  (if (vector? ?data)
    (let [[old-state-map new-state-map] ?data]
      (if (:first-open? new-state-map)
        (log/trace "Channel socket successfully established!: " new-state-map)
        (do
          (log/trace "Channel socket state change: " old-state-map)
          (log/trace "Channel socket state change: " new-state-map))))
    (log/error "Sente realtime engine failed, returned ?data should be a vector")))

(defmethod -event-msg-handler
  [:chsk/recv :chsk/ws-ping]
  [{:keys [_?data]}]
  (rt/chsk-send! [:chsk/pong]))

(defmethod -event-msg-handler
  [:chsk/handshake nil]
  [{:as _ev-msg :keys [?data]}]
  (let [[_?uid _?csrf-token _?handshake-data] ?data]
    (log/trace "Handshake: " ?data)))
