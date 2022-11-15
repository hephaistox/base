(ns base.data-structure.util
  "Utility for data structure")

;; See https://gist.github.com/danielpcox/c70a8aa2c36766200a95
(defn deep-merge
  "Deep merge nested maps"
  [& maps]
  (apply merge-with (fn [& args]
                      (if (every? #(or (map? %) (nil? %)) args)
                        (apply deep-merge args)
                        (last args)))
         maps))

(defn idx-of
  "Return the index of the first found value in the sequence"
  [v value]
  (ffirst
   (filter #(= value (second %))
           (map-indexed vector v))))

(defn idx-of-pred
  "Same as idx-of but with a predicate"
  [v pred]
  (when (and pred
             (fn? pred))
    (ffirst
     (filter #(pred (second %))
             (map-indexed vector v)))))
