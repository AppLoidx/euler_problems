(ns euler-problems.solution-2-1)

(defn pow [a b] (reduce * 1 (repeat b a)))

(defn digits [number]
  (map #(- (int %) 48) (str number)))

(defn can-be-written-as-sum-of-fifths? [number]
  (and (not (= number 1)) (= (apply + (map #(pow % 5) (digits number))) number)))

(println (reduce + (filter can-be-written-as-sum-of-fifths? (range 500000))))