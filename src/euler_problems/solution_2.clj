(ns euler-problems.solution-2
  (:use [util.misc]))

(defn digits
  ([n] (digits n 10))
  ([n ^long radix]
   (loop [n n
          res nil]
     (if (zero? n)
       res
       (recur
         (quot n radix)
         (cons (rem n radix) res))))))

(defn iexpt [x ^long exp]
  "Raise x to an integer exponent, of the form x^exp"
  (reduce * (repeat exp x)))

(defn sum-of-powers [pow n]
  (reduce + (map #(iexpt % pow) (digits n))))

(defn calc [coll]
  (reduce +
          (filter #(= % (sum-of-powers 5 %))
                  coll)))

(defn solve [batch-size limit]
  (reduce + (pmap calc (partition-all batch-size (range 32 limit)))))

(time (solve 15000 (* 6 (iexpt 9 5))))