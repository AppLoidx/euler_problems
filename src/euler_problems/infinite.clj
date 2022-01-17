(ns euler-problems.infinite)

(defn take-nums
  [x]
  (take x (filter #(zero? (min (mod % 3) (mod % 5))) (range))))

(println (apply + (take-nums 10)))