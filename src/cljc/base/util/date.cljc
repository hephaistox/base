(ns base.util.date
  "Utility function for date management"
  #?(:cljs (:require [cljs-time.core :as cljs-time])))

#?(:clj (def year-format
          (java.text.SimpleDateFormat. "yyyy")))

#?(:clj (defn this-year
          []
          (.format (java.util.Date.)))
   :cljs (defn this-year
           []
           (cljs-time/year
            (cljs-time/now))))
