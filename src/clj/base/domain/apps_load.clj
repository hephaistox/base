(ns base.domain.apps-load
  "Domain for application.
  That domain describes application in the mono repo,
  Apps could require each other, they describe the conditions to
  launch, test, initialize an application"
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]

   [base.domain.apps :as apps]
   ))

(defn load-cfg
  "Load the `apps.edn` file, validate it"
  ([]
   (load-cfg nil))
  ([apps-filename]
   (try
     (let [apps (edn/read-string (slurp (io/resource (or apps-filename "apps.edn"))))]
       (try
         (apps/validate apps)
         apps
         (catch Exception e
           (throw (ex-info "Unable to load app setup"
                           {:cause e})))))
     (catch java.lang.IllegalArgumentException e
       (throw (ex-info "Unable to load apps file"
                       {:cause e
                        :apps-filename apps-filename}))))))
