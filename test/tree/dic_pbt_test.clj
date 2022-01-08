(ns tree.dic_pbt_test
  (:require [clojure.test.check.generators :as gen]
            [clojure.test :refer [is deftest]]))


(require '[clojure.test.check :as tc]
         '[clojure.test.check.properties :as prop])

(def sort-idempotent-prop
  (prop/for-all [v (gen/vector gen/small-integer)]
                (= (sort v) (sort (sort v)))))

(deftest simple_check

  (is (= (:result (tc/quick-check 100 sort-idempotent-prop)) true)))
