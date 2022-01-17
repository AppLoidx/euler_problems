(ns euler-problems.inf-3)

(defn multiple-of-any? [coll n]
  (some #(zero? (rem n %)) coll))

(defn another-one
  [nums x]
  (->> (range)
       (filter (partial multiple-of-any? nums))
       (take x)
       (reduce +)))

(println (another-one [3 5] 10))
