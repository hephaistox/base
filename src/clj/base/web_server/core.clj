(ns base.web-server.core
  "Create the webserver, (http kit), set it up, log uncaught exceptions in thread"
  (:require
   [base.util.conf :as conf]
   [base.log :as log]

   [org.httpkit.server :as http-kit]
   ))

;; Log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ thread ex]
     (log/error {:what :uncaught-exception
                 :exception ex
                 :where (str "Uncaught exception on" (.getName thread))}))))

(defn start-server
  "Generate the server, based on the given handler.
  Options are optional, default values are
  - dont-block [true] tells server not to block the thread
  - port: [3636] default port - should not be used to enable multiple web servers in the repl
  "
  [handler opts]
  (log/info "Starting Webserver component")
  (let [opts-with-defaults (merge
                            {:port (conf/read-param [:http-server :port])
                             :dont-block false}
                            opts)
        webserver-opts {:port (:port opts-with-defaults)
                        :join? (:dont-block opts-with-defaults)}]
    (log/trace "Params" opts-with-defaults)
    (try
      (http-kit/run-server handler
                           webserver-opts)
      (catch Exception e
        (log/error "Unable to start server" e))))
  (log/trace "End of Webserver component"))

(defn stop-server
  [server]
  (server)
  (log/info "Server stopped")
  server)
