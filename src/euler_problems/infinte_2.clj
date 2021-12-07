(ns euler-problems.infinte-2)

(defn take-nums
  [x]
  (take x (filter #(zero? (min (mod % 3) (mod % 5))) (range)))
  )

(defn multiple-of-any? [coll n]
  "Returns whether n is multiple of any number in coll."
  (some #(zero? (rem n %)) coll))

(defn another-one
  [x]
  (
    ->> (range)
        (filter (partial multiple-of-any? [3 5]))
        (take x)
        (reduce +))
  )


(println (another-one 10))
