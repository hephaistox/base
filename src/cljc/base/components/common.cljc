(ns base.components.common
  "Common utilities function"
  (:require
   [clojure.string :as str]
   ))

(defn combine-classes
  "Combine css classes together"
  [& args]
  (str/join " " args))


(defn combine-classes-with-custom
  "Add the default css values
  `type` could be
  * :merge and custom css is added to default (custom is higher priority)
  * :custom-only, use only custom css
  * by default (:default-only) is not changing the default behavior
  "
  [custom-css type & default-css]
  (case type
    :merge (combine-classes (apply combine-classes default-css)
                            custom-css)
    :custom-only custom-css
    ;:default-only
    (apply combine-classes default-css)))
