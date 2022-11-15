(ns base.util.url
  "Manage url")

;; From lambdaisland.url/url-regex
(def url-regex #?(:clj #"\A(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*)?(?:\?([^#]*))?(?:#(.*))?\z"
                  :cljs #"^(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*)?(?:\?([^#]*))?(?:#(.*))?$"))

(defn extract-path
  "Extract the path from the url"
  [url]
  (first
   (re-seq url-regex
           url)))

(defn get-protocol
  "Extract the protocol from the url"
  [url]
  (get (extract-path url) 1))

(defn get-path
  "Extract the path from the url"
  [url]
  (get (extract-path url) 2))

(defn get-static-page
  "Extract the static path from the url"
  [url]
  (get (extract-path url) 3))

(defn get-parameters
  "Extract the parameters from the url"
  [url]
  (get (extract-path url) 4))

(defn get-spa-page
  "Extract the spa page from the url"
  [url]
  (get (extract-path url) 5))


(def uri-regex #?(:clj #"\A(?:(?:[^:/?#]+):)?(?://(?:[^/?#]*))?(.*)\z"
                  :cljs #"^(?:(?:[^:/?#]+):)?(?://(?:[^/?#]*))?(.*)$"))

(defn get-uri
  "Return uri"
  [url]
  (second
   (first
    (re-seq uri-regex
            url))))
