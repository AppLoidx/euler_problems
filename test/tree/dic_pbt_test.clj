(ns tree.dic_pbt_test
  (:require [clojure.test.check.generators :as gen]
            [clojure.test :refer [is deftest]]
            [tree.Dictionary :refer :all]))


(require '[clojure.test.check :as tc]
         '[clojure.test.check.properties :as prop])

;; pbt 1
(def find-node-check_pbt
  (prop/for-all [v (gen/vector gen/small-integer)]
                (= v (:value (find-node (create-dict {:my-key v}) :my-key)))))

(deftest simple_check
  (is (= (:result (tc/quick-check 100 find-node-check_pbt)) true)))

;; bad test
(def height_pbt
  (prop/for-all [v (gen/vector gen/small-integer)]
                (= 1 (height (remove-node (create-dict {:my-key v :another-key v}) :my-key)))))

(deftest height_test
  (is (= (:result (tc/quick-check 100 height_pbt)) true)))


(def find-node-nil_pbt
  (prop/for-all [v (gen/vector gen/small-integer)]
                (= nil (find-node (remove-node (create-dict {:my-key v :another-key v}) :my-key) :my-key))))

(deftest find-node-nil_test
  (is (= (:result (tc/quick-check 100 find-node-nil_pbt)) true)))

(def dfilter_pbt
  (prop/for-all [v (gen/vector gen/small-integer)]
                (= 2 (count (dfilter #(= v (val (first %))) (create-dict {:my-key v :another-key v}))))))

(deftest dfilter_test
  (is (= (:result (tc/quick-check 100 dfilter_pbt)) true)))

