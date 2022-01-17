(ns euler-problems.solution-2-fast)

(defn solve-fast []
  (let [pow5 (apply hash-map (interleave (seq "0123456789")
                                         (map #(Math/pow % 5) (range 10))))
        max  (-> \9 pow5 (* 6))]
    (reduce + (filter #(= % (reduce + (map pow5 (str %))))
                      (range 2 max)))))

(println (solve-fast))
