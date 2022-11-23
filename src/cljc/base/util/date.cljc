(ns base.util.date
  "Utility function for date management"
  #?(:cljs (:require [cljs-time.core :as cljs-time])))

#?(:clj (def year-format
          (java.text.SimpleDateFormat. "yyyy")))

#?(:clj (defn this-year
          ([date]
           (.format year-format date))
          ([]
           (this-year (java.util.Date.))))
   :cljs (defn this-year
           ([date]
            (cljs-time/year date))
           ([]
            (this-year (cljs-time/now)))))
