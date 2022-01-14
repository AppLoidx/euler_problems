#!/usr/bin/env lein exec

(ns lab3.aprox
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))


(defn binary-search
  "  Finds index of rightmost value in sorted vector that is less or equal to given value."
  [vec value]
  (loop [left 0
         right (dec (count vec))]
    (if (= (- right left) 1)
      (if (<= (nth vec right) value) right left)
      (let [middle (quot (+ left right) 2)]
        (if (<= (nth vec middle) value)
          (recur middle right)
          (recur left middle))))))


(defn find-segment
  "  Finds indices of segment that contains given point.
   Params:
     xs - sorted coordinates of segments
     x - point"
  [xs x]
  (min (- (count xs) 2)
       (binary-search xs x)))

; interpolations
; linear

(defn- calc-line
  "Finds value in point x.
   Given:
     f(xl) = yl
     f(xr) = yr
     xl <= x <= xr"
  [xl yl xr yr x]
  (let [xl (double xl)
        yl (double yl)
        xr (double xr)
        yr (double yr)
        x (double x)
        coef (/ (- x xl) (- xr xl))]
    (+ (* (- 1.0 coef) yl)
       (* coef yr))))

(defn linear_inter
  "Interpolates set of points using linear interpolation."
  [points]
  (let [xs (mapv first points)
        ys (mapv second points)]
    (fn [x]
      (let [ind-l (find-segment xs x)
            ind-r (inc ind-l)]
        (calc-line (xs ind-l) (ys ind-l) (xs ind-r) (ys ind-r) x)))))

; ------------------------------------------

; poly

(defn poly_inter
  "Interpolates point by polynomial using Newton form.
   http://en.wikipedia.org/wiki/Newton_polynomial"
  [points]
  (let [xs (mapv first points)
        ys (map second points)
        divided-difference (fn [[f1 f2]]
                             {:f (/ (- (:f f2) (:f f1))
                                    (- (:x-r f2) (:x-l f1)))
                              :x-r (:x-r f2)
                              :x-l (:x-l f1)})
        next-level-differences (fn [fs]
                                 (doall (map divided-difference (partition 2 1 fs))))
        fs (->> (map (fn [x f] {:f f :x-l x :x-r x}) xs ys)
                (iterate next-level-differences)
                (take (count xs))
                (map first)
                (mapv :f))]
    (fn [^double x]

      (loop [sum (double 0)
             pow (double 1)
             ind 0]
        (if (= (count fs) ind)
          sum
          (recur (+ (* (fs ind) pow) sum)
                 (* pow (- x (xs ind)))
                 (inc ind)))))))

; ------------------------------------------------

(defn- validate-unique [xs]
  (when-not (apply distinct? xs)
    (throw (IllegalArgumentException. "All x must be distinct."))))


(defn interpolate
  [type points]
  (let [method (case type
                 :linear linear_inter
                 :polynomial poly_inter)
        points (sort-by first points)]
    (validate-unique (map first points))
    (method points)))

;; (prn ((interpolate [[1, 1], [2, 2], [3, 4]] :polynomial) 10))

(defn apply-to-file-data
  [f]
  (with-open [rdr (io/reader *in*)]
    (reduce f [] (line-seq rdr))))

(defn parse-csv
  [line]
  (mapv read-string (str/split line #",")))

(defn parse-keyword
  []
  (keyword (first (drop 1 *command-line-args*))))


(defn parse-discretization
  []
  (read-string (first (drop 2 *command-line-args*))))

(defn combiner
  ([list line]
   (let [points (merge list (parse-csv line))
         aprox-type (parse-keyword)
         discretization (parse-discretization)
         aprox-func (interpolate aprox-type points)
         range-min (apply min (mapv first points))
         range-max (apply max (mapv first points))
         ]

     (prn (mapv aprox-func (range range-min range-max discretization)))

     points)))


(defn main []
     (prn (apply-to-file-data combiner))
  )

(main)