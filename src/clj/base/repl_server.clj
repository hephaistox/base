(ns base.repl-server
  "REPL component"
  (:require
   [clojure.java.io :as io]

   [cider.nrepl :as nrepl-mw]
   [nrepl.server :refer [default-handler start-server stop-server]]

   [base.log :as log]
   [base.util.conf :as conf]
;
   ))

(defn custom-nrepl-handler
  "We build our own custom nrepl handler"
  [nrepl-mws]
  (apply default-handler
         nrepl-mws))

(def repl
  "Store the repl instance in the atom"
  (atom {}))

(defn get-nrepl-port-parameter
  []
  (conf/read-param [:dev :clj-nrepl-port]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn get-active-nrepl-port
  "Retrieve the nrepl port, available for REPL"
  []
  (:nrepl-port @repl))

(defn stop-repl
  "Stop the repl"
  [repl-port]
  (log/info "Stop nrepl server on port" repl-port)
  (stop-server (:repl @repl))
  (io/delete-file ".nrepl-port")
  (reset! repl {}))

(defn start-repl*
  "Launch a new repl"
  [{:keys [nrepl-port middleware]}]
  (let [repl-port (or nrepl-port
                      (get-nrepl-port-parameter))
        ]
    (reset! repl {:nrepl-port nrepl-port
                  :repl (do
                          (log/info "nrepl available on port " repl-port)
                          (spit ".nrepl-port" repl-port)
                          (start-server :port repl-port
                                        :handler (custom-nrepl-handler middleware)))})

    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. #(do
                                  (log/info "SHUTDOWN in progress" repl-port)
                                  (stop-repl repl-port))))))

(defn start-repl
  "Start repl, setup and catch errors"
  []
  (try
    (start-repl* {:nrepl-port 4000
                  :middleware (conj nrepl-mw/cider-middleware 'refactor-nrepl.middleware/wrap-refactor)})
    :started
    (catch Exception e
      (log/error "Uncaught exception" e))))
