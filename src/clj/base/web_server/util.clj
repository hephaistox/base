(ns base.web-server.util
  "Utility functions for webserver"
  (:require
   [clojure.string :as str]
   ))

(defn- kw-to-js
  "Transform a keyword in a javascript compatible name"
  [k]
  (str
   "\""
   (str/replace (name k) #"-" "_")
   "\""))

(defn mapToJSmap
  "Transform a map to a javascript map"
  [m]
  (format "{%s}"
          (str/join ",\n"
                    (for [[k v] m]
                      (str
                       (kw-to-js k)
                       ": "
                       (cond (string? v) (str "\"" v "\"")
                             (keyword? v) (kw-to-js v)
                             :else v))))))
