(ns base.user
  "Commonly used symbols for easy access in the ClojureScript REPL during
  development."
  (:require
;;   [cljs.repl :refer (Error->map apropos dir doc error->str ex-str ex-triage
;;                                 find-doc print-doc pst source)]
   [clojure.pprint :refer (pprint)]
   [clojure.string :as str]
   [reagent.dom :as rdom]
   ))

(defn render-id
  [app-id component]
  (let [el (.getElementById js/document app-id)]
    (rdom/unmount-component-at-node el)
    (rdom/render [component] el)))

(defn render-ids
  [& ids]
  (loop [[app-id component & r] ids]
    (render-id app-id component)
    (when r
      (recur r))))

(comment
  (pprint (str/trim "This line suppresses some clj-kondo warnings.")))
