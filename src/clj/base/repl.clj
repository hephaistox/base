(ns base.repl
  "REPL component"
  (:require
   [nrepl.server :refer [default-handler start-server stop-server]]
   [base.util.conf :as conf]
   [clojure.java.io :as io]
   [base.log :as log]))

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

(defn get-active-nrepl-port
  "Retrieve the nrepl port"
  []
  (:nrepl-port @repl))

(defn stop-repl
  "Stop the repl"
  [repl-port]
  (log/info "Stop nrepl server on port" repl-port)
  (stop-server (:repl @repl))
  (io/delete-file ".nrepl-port")
  (reset! repl {}))

(defn start-repl
  "Launch a new repl"
  [nrepl-mws {:keys [nrepl-port]}]
  (let [repl-port (or nrepl-port
                      (get-nrepl-port-parameter))]
    (reset! repl {:nrepl-port nrepl-port
                  :repl (do
                          (log/info "nrepl available on port " repl-port)
                          (spit ".nrepl-port" repl-port)
                          (start-server :port repl-port
                                        :handler (custom-nrepl-handler nrepl-mws)))})

    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. #(do
                                  (log/info "SHUTDOWN in progress" repl-port)
                                  (stop-repl repl-port))))))
