(ns tree.dic_test
  (:require [clojure.test :refer [deftest is]]
            [tree.Dictionary :refer :all]))

(deftest create-dict-test
  (is
   (=
    {:key :666, :value 777, :left nil, :right nil}
    (create-dict {:666 777}))))

(deftest create-nil-dict-test
  (is
   (nil?
    (create-dict nil))))

(deftest add-to-dict-test
  (is
   (=
    {:key :666, :value 100, :left {:key :111, :value 1500, :left nil, :right nil}, :right nil}
    (add-to-dict (create-dict {:666 100}) {:111 1500}))))

(deftest add-to-nil_test
  (is
   (=
    {:key :666, :value 100, :left nil, :right nil}
    (add-to-dict nil {:666 100}))))

(deftest add-nil-to-dict-test
  (is
   (=
    {:key :555, :value 0, :left nil, :right nil}
    (add-to-dict (create-dict {:555 0}) nil))))

(deftest add-existing-value-test
  (is
   (=
    {:key :666, :value -20, :left {:key :111, :value 1500, :left nil, :right nil}, :right nil}
    (add-to-dict (add-to-dict (create-dict {:666 100}) {:111 1500}) {:666 -20}))))

(deftest get-value-test
  (is
   (=
    1500
    (value-from-dict (add-to-dict (create-dict {:666 100}) {:111 1500}) :111))))


(deftest get-non-existing-value-test
  (is
   (=
    nil
    (value-from-dict (add-to-dict (create-dict {:666 100}) {:111 1500}) :0))))

(deftest simple-height-test
  (is
   (=
    2
    (height (add-to-dict (create-dict {:666 100}) {:111 1500})))))

(deftest bidirectional-tree-height-test
  (is
   (=
    2
    (height (add-to-dict (add-to-dict (create-dict {:666 100}) {:111 1500}) {:1000 1000})))))

(deftest bidirectional-tree-height-3-test
  (is
   (=
    3
    (height (add-to-dict
             (add-to-dict
              (add-to-dict
               (create-dict {:666 100})
               {:111 1500})
              {:1000 1000})
             {:2000 -1})))))

