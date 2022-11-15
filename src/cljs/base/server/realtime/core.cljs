(ns base.server.realtime.core
  (:require
   [taoensso.sente :as sente]

   [base.log :as log]
   [base.server.cst :as cst]
   [base.session :as session]
   ))

(if session/?csrf-token
  (log/info "Init of realtime component, token is:" session/?csrf-token)
  (log/error "Fail during realtime component, CSRF token not found"))

(try
  (let [{:keys [chsk ch-recv send-fn state]}
        (sente/make-channel-socket-client!
         cst/realtime-uri
         session/?csrf-token
         {:type :auto ; e/o #{:auto :ajax :ws}
          :packer :edn})]

    (def chsk       chsk)
    (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
    (def chsk-send! send-fn) ; ChannelSocket's send API fn
    (def chsk-state state)   ; Watchable, read-only atom
    )
  (catch js/Object e
    (log/error "Unexpected error during realtime component init" e)))

(defn get-chsk
  []
  chsk)
