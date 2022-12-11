(ns base.user
  "Will be `refer :all` in the subproject `user` namespace, default namepsace for subproject REPL"
  (:require
   [clojure.edn :as edn]
   [clojure.tools.namespace.repl :as tn]

   [clj-memory-meter.core :as mm]
   [mount.core :as mount]
   [outpace.config.repl]
   [prone.middleware :as prone]
   [ring.middleware.reload :as mr]

   [base.log :as log]
   [base.repl-server :as repl]
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
  (repl/start-repl))

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

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn go
  "starts all states defined by defstate"
  []
  (outpace.config.repl/reload)
  (start)
  :ready)

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn reset
  "stops all states defined by defstate, reloads modified source files, and restarts the states"
  []
  (stop)
  #_(tn/refresh :after 'dev/go))

;; See https://github.com/clojure-goes-fast/clj-memory-meter, for details
 #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
 (defn measure-time-sample
  []
  (mm/measure "hello world"))
