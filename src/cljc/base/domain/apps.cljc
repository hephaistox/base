(ns base.domain.apps
  "Domain for apps, a technical domain describing the list of application in the monorepo"
  (:require
   [malli.core :as m]
   [malli.error :as me]

   [base.domain.apps.schema :as apps-schema]
   ))

(defn validate
  "Validate the app against its namespace"
  [apps]
  (when-not (m/validate apps-schema/apps apps)
    (throw (ex-info "Invalid app"
                    {:apps apps
                     :validation (me/humanize (m/explain apps-schema/apps apps))})))
  true)

(defn apps-names
  "Return sequence of names of the app"
  [apps]
  (map :app-name apps))

(defn app-get-by-name
  "Check that the given app is really part of our apps and return it"
  [apps app-name]
  (first
   (filter #(= app-name (:app-name  %)) apps)))

(defn runnables
  "Runnables applications"
  [apps]
  (map :app-name
       (filter :runnable? apps)))
