(ns base.domain.apps.schema
  "Defines what an application is")

(def app
  "The application malli schema description.
  An application have an app-name.
  The react? tells if we can run react on it
  Runnable tells if the application require a deps.edn executable"
  [:map {:closed true}
   [:app-name :string]
   [:react? :boolean]
   [:testable? :boolean]
   [:runnable? :boolean]])

(def apps
  "Sequence of app"
  [:sequential app])
