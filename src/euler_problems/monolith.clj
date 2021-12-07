(ns euler-problems.monolith)

(defn solve []
  ;; use precomputed 5th powers for each digit
  (let [powers [0 1 32 243 1024 3125 7776 16807 32768 59049]]
    (apply + (filter (fn [x] (= x (apply + (map #(nth powers %)
                                                (loop [y x result nil]
                                                  (if (<= y 0)
                                                    result
                                                    (recur (int (/ y 10))
                                                           (cons (rem y 10) result))))))))
                     (range 2 1000000)))))

(println (solve))