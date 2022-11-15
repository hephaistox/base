(ns base.endpoint.realtime.core
  "Add a real time communication feature with the client"
  (:require
   [mount.core :refer [defstate] :as mount]
   [taoensso.sente :as sente]
   [taoensso.sente.server-adapters.http-kit :refer [get-sch-adapter]]

   [base.endpoint.realtime.message-handler :as message-handler]
   [base.log :as log]
   [base.util.conf :as conf]
   ))

(defn start-rt
  [debug?]
  (let [{:keys [ch-recv] :as sente-map} (sente/make-channel-socket! (get-sch-adapter)
                                                                    {:user-id-fn (constantly 123456)})
        server (sente/start-server-chsk-router! ch-recv
                                                message-handler/event-msg-handler)]
    (when (= :dev! debug?)
      (reset! sente/debug-mode?_ true))
    (assoc sente-map
           :server server)))

;; See for an example https://github.com/ptaoussanis/sente/tree/master/example-project/src/example
(defstate realtime-server
  "Contains a map with the following keys:
* ajax-get-or-ws-handshake-fn
* ajax-post-fn
* ch-recv - Receive channel
* connected-uids - Watchable, read-only atom
* send-fn - ChannelSocket's send API fn
* server is the sente server, useful to stop it especially"
  :start (try
           (log/info "Starting realtime server component")
           (let [res (start-rt (conf/read-param [:env]))]
             (log/trace "Realtime server component is started")
             res)
           (catch Exception e
             (log/error "Unexpected error during realtime start" e)))
  :stop (when @realtime-server
          (when-let [server (:server @realtime-server)]
            (server))))

(defn ajax-get-or-ws-handshake
  "Is the handshake handler"
  []
  (if-let [afn (:ajax-get-or-ws-handshake-fn @realtime-server)]
    afn
    (throw (ex-info "Realtime server is not started" {}))))

(defn ajax-post
  "Post a message in ajax mode"
  []
  (if-let [afn (:ajax-post-fn @realtime-server)]
    afn
    (throw (ex-info "Realtime server is not started" {}))))

(defn ch-recv
  "Receive a message handler"
  []
  (if-let [afn (:ch-recv @realtime-server)]
    afn
    (throw (ex-info "Realtime server is not started" {}))))

(defn connected-uids
  "List of all connected clients"
  []
  (if-let [afn (:connected-uids @realtime-server)]
    afn
    (throw (ex-info "Realtime server is not started" {}))))

(defn send-fn
  "Send a message handler"
  [& args]
  (if-let [afn (:send-fn @realtime-server)]
    (apply afn args)
    (throw (ex-info "Realtime server is not started" {}))))

(comment
  (connected-uids)
  (send-fn "hi!!")
;
  )
