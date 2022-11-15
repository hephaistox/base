(ns base.endpoint.session
  "Manage session, stored in memory for now
  Session information are described there : https://github.com/ring-clojure/ring/wiki/Sessions"
  (:require
   [ring.middleware.session]
   [ring.middleware.session.memory :as mem]
   ))

(def session-store
  "Atom containing all active sequences"
  (atom {}))

(comment
  @session-store
;
  (reset! session-store {})
  (swap! session-store assoc :foo :bar))

(def options
  "Options "
  {:store (mem/memory-store session-store)
   :cookies-attrs {:http-only true}})

(defn sessions
  "Return sessions"
  []
  @session-store)

(defn wrap-session
  "Wrapper to read in the request:
  * the session key in :headers \"cookie\" \"ring-session=xxx\", read session data in memory, and store it in :session
  * the respond can contains an updated :session key which will be saved to the cookie"
  [handler]
  (ring.middleware.session/wrap-session handler
                                        options))
