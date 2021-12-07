(ns euler-problems.module_solution)

(defn multiple-of-any? [coll n]
  (some #(zero? (rem n %)) coll))

(defn generate-nums [x] (range x))

(defn filter-by-nums [a b arr]
  (filter (partial multiple-of-any? [a b]) arr))

(defn collect-all-nums [arr]
  (reduce + arr))

(defn solve []
  "Finds the sum of all the multiples of 3 or 5 below 1000."
  (collect-all-nums (filter-by-nums 3 5 (generate-nums 1000))))


; Expected result: 233168
(println (solve))