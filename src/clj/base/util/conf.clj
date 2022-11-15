(ns base.util.conf
  "Configuration parameters, stored in configuration file.
  This namespace is the entry point to call conf

  For a parameter `p`:
  * Create the parameter, in the current implemenation, in the `util/conf.clj`
  * Read the parameter with  `conf/read-param`"
  (:require
   [mount.core :refer [defstate]]

   [base.log :as log]
   [base.util.conf.outpace :as conf-op]
   [base.util.conf.protocol :as prot]
   ))

;; Options:
;;     See http://realworldclojure.com/application-configuration/
;; Design decision: outpace is kept as a configuration management tool.
;;     The objectives we have: be able to have local deps, environment dependent, able to get environment variables, with a centralized and controlled values
;;     Outpace is chosen for that, gathering declarations in the same namespace prevent adherance to the lib,
;;     Its configuration file and the namespace allow a centralized definition of the parameters

(defn start-conf []
  (log/info "Starting configuration component")
  (let [conf (conf-op/->OutpaceConf {})]
    (log/trace "Configuration component is started")
    conf))

(defn stop-conf []
  (log/debug "Stop configuration component"))

(defstate conf-state
  :start (start-conf)
  :stop (stop-conf))

(defn read-param [key-path]
  (when-not (vector? key-path)
    (throw (ex-info "Key path should be a vector. I found " key-path)))
  (let [value (when-not (instance? mount.core.NotStartedState @conf-state)
                (prot/read-conf-param @conf-state key-path))]
    (log/trace "Read key-path" key-path "= " value)
    value))
