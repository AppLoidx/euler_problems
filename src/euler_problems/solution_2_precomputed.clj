(ns euler-problems.solution-2-precomputed)

(defn digits [n]
  "Returns a sequence of the digits in the number n, in big-endian order"
  (loop [x n result nil]
    (if (<= x 0)
      result
      (recur (int (/ x 10)) (cons (rem x 10) result)))))

(defn solve []
  ;; use precomputed 5th powers for each digit
  (let [powers [0 1 32 243 1024 3125 7776 16807 32768 59049]]
    (apply + (filter (fn [x]
                       (= x (apply + (map #(nth powers %) (digits x)))))
                     (range 2 1000000)))))

(println (solve))