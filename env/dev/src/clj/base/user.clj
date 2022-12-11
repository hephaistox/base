(ns base.user
  "Default namespace in the repl,
  Contains all necessary functions to start the app"
  (:require
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.tools.namespace.repl :as tn]

   [cider.nrepl :as nrepl-mw]
   [clj-memory-meter.core :as mm]
   [mount.core :as mount]
   [outpace.config.repl]
   [prone.middleware :as prone]
   [ring.middleware.reload :as mr]

   [base.log :as log]
   [base.repl :as repl]
;
   ))

(def runnables
  "List of directories"
  (edn/read-string
   (slurp
    "base_dirs.edn")))

(defn wrap-nocache
  "Dev wrapper to prevent caching"
  [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn wrap-reload
  "Reload clj as they are saved"
  [handler]
  (let [dirs (mapcat (fn [v]
                       [(str v "/src/clj")
                        (str v "/src/cljc")])
                     runnables)]
    (log/info "Listen to the following dirs: " (pr-str dirs))
    (-> handler
        (mr/wrap-reload {:dirs dirs}))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn dev-middlewares
  "Add dev middlewares"
  [handler]
  (-> handler
      wrap-reload
      wrap-nocache
      (prone/wrap-exceptions {:app-namespaces runnables})))

(defn start
  "Start repl"
  [& _args]
  (try
    (let [middleware (conj nrepl-mw/cider-middleware 'refactor-nrepl.middleware/wrap-refactor)]
      (repl/start-repl middleware
                       {:nrepl-port 4000}))
    :started
    (catch Exception e
      (log/error "Uncaught exception" e))))

#_(defn start []
  ;(with-logging-status)
    #_(mount/start #'app.conf/config
                   #'app.db/conn
                   #'app.www/nyse-app
                   #'app.example/nrepl))             ;; example on how to start app with certain states

(defn stop []
  (mount/stop))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn refresh []
  (stop)
  (tn/refresh))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn refresh-all []
  (stop)
  (tn/refresh-all))

(defn go
  "starts all states defined by defstate"
  []
  (outpace.config.repl/reload)
  (start)
  :ready)

(defn reset
  "stops all states defined by defstate, reloads modified source files, and restarts the states"
  []
  (stop)
  #_(tn/refresh :after 'dev/go))

;; See https://github.com/clojure-goes-fast/clj-memory-meter, for details
(defn measure-time-sample
  []
  (mm/measure "hello world"))
