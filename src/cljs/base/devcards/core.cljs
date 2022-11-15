(ns base.devcards.core
  "Devcards present components so they're seen / tested"
  [:require
   [cljsjs.react]
   [cljsjs.react.dom]
   ;; devcards needs cljsjs.react and cljsjs.react.dom to be imported
   [cljsjs.highlight]
   [cljsjs.marked]

   [devcards.core :as dc]
   [mount.core :as mount]

   [base.devcards.alert]
   [base.devcards.button]
   [base.devcards.footer]
   [base.devcards.header]
   [base.devcards.menu]
   [base.devcards.menu-item]
   [base.devcards.svg-icons]
   [base.devcards.version]
   ])

(defn ^:export
  init
  "Entry point for starting devcards"
  []
  (mount/in-cljc-mode)
  (mount/start)
  (dc/start-devcard-ui!)
  )
