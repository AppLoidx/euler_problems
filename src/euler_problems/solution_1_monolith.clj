(ns euler-problems.solution-1-monolith)

(defn solve [n]
  (letfn [(f [m r] (range m r m))]
    (reduce + (distinct (concat (f 3 n) (f 5 n))))))

(println (solve 1000))