(ns euler-problems.inf4)

(defn multiple-of-any? [coll n]
  (some #(zero? (rem n %)) coll))

(defn another-one
  [nums]
  (
    ->> (range)
        (filter (partial multiple-of-any? nums))
        )
  )


(println (take 10 (another-one [3 5])))
