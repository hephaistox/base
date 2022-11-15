(ns base.blob.load
  "Get a blob file.
  Is now stored locally, could be stored in a more robust manner"
  (:require
   [clojure.edn :as edn]

   [base.log :as log]
   ))

(defn load-edn
  "Load a file by its name"
  [file-name]
  (try
    (edn/read-string
     (slurp file-name))
    (catch Exception _e
      (log/error "Unable to load file"))))
