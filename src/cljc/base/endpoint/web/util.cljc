(ns base.endpoint.web.util
  (:require
   [clojure.string :as str]
   ))

(defn parse-cookie
  [cookie-text]
  (when (and cookie-text
             (not= "" cookie-text))
    (into {}
          (map (fn [line]
                 (vec
                  (let [res (str/split line #"=")]
                    (case (count res)
                      1 [res nil]
                      2 res
                      [ (first res) (vec (rest res))]))))
               (str/split cookie-text #";")))))
