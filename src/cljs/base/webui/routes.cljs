(ns base.webui.routes
  (:require
   [reitit.core :as reitit]
   [pushy.core :as pushy]))

;; https://github.com/kibu-australia/pushy
;; https://cljdoc.org/d/metosin/reitit/0.5.18/api/reitit.frontend?q=match-by#match-by-name

(defn parse
  "From the `routes` parameter, parse the `url` parameter, to return the route content"
  [routes url]
  (get-in (reitit/match-by-path routes
                                url)
          [:data :name]))

(defn url-for
  "Return the path from the given `panel-kw` in the `routes` definition"
  [routes panel-kw]
  (reitit/match-by-name routes panel-kw))

(defn navigate!
  "Go to the path associated with the `handler` keyword"
  [history routes handler]
  (pushy/set-token! history
                    (url-for routes handler)))
