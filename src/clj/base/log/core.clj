;;#!/usr/bin/env bb
(ns base.log.core
  "Create a log file for bb"
  (:require
   [babashka.fs :as fs]))

(def log-path "log")

(defn log-date
  ([]
   (log-date (new java.util.Date)))
  ([d]
   (clojure.core/format "%tZ_%ty-%tm-%td_%tH:%tM:%ts" d d d d d d d)))

(defn build-logfilename
  "Generate a filename"
  ([filename]
   (build-logfilename filename (new java.util.Date)))
  ([filename date]
   (->>
    date
    log-date
    (format "%s-%s.log" filename)
    (fs/absolutize)
    str)))

(comment
  (build-logfilename "toto"))
