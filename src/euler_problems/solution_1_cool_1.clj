(ns euler-problems.solution-1-cool-1)

(println  (apply + (filter #(zero? (min (mod % 3) (mod % 5))) (range 1000))))