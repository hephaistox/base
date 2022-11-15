(ns base.log)

(defn info
  [& msg]
  (apply js/console.log "I:" msg))

(defn warn
  [& msg]
  (apply js/console.log "W:" msg))

(defn trace
  [& msg]
  (apply js/console.log "T:" msg))

(defn error
  [& msg]
  (apply js/console.log "E:" msg))
