(ns lab3.aprox-test
  (:require [clojure.test :refer [deftest is]]
            [lab3.aprox :refer :all]))

(def data `("1,1" "2,2" "3,3"))

(deftest create-dict-test
  (is
   (=
    `[1.0 1.5 2.0 2.5]
    (with-redefs-fn {#'lab3.aprox/parse-keyword (fn [] :linear)
                     #'lab3.aprox/parse-discretization (fn [] 0.5)}
      #(interp

        data)))))
