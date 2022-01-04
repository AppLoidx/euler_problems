(ns tree.dic-test
  (:require [clojure.test :refer [deftest is]]
            [tree.Dictionary :refer :all]))

(deftest create-dict-test
  (is
   (=
    {:left nil, :right nil, :key 666, :value 777}
    (create-dict {:key 666, :value 777}))))

(deftest create-nil-dict-test
  (is
   (=
    {:left nil, :right nil, :key nil, :value nil}
    (create-dict nil))))

(deftest add-to-dict-test
  (is
   (=
    {:left
     {:left nil, :right nil
      :key  111, :value 1500}
     :right nil
     :key   666
     :value 100}
    (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}))))

(deftest add-to-nil-test
  (is
   (=
    {:left nil, :right nil
     :key  666, :value 100}
    (add-to-dict nil {:key 666 :value 100}))))

(deftest add-nil-to-dict-test
  (is
   (=
    {:left nil, :right nil
     :key  555, :value 0}
    (add-to-dict (create-dict {:key 555 :value 0}) nil))))

(deftest add-existing-value-test
  (is
   (=
    {:left
     {:left nil, :right nil
      :key  111, :value 1500}
     :right nil
     :key   666
     :value -20}
    (add-to-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) {:key 666 :value -20}))))

(deftest get-value-test
  (is
   (=
    1500
    (value-from-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) 111))))


(deftest get-non-existing-value-test
  (is
   (=
    nil
    (value-from-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) 0))))

(deftest simple-height-test
  (is
   (=
    2
    (height (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500})))))

(deftest bidirectional-tree-height-test
  (is
   (=
    2
    (height (add-to-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) {:key 1000 :value 1000})))))

(deftest bidirectional-tree-height-3-test
  (is
   (=
    3
    (height (add-to-dict
             (add-to-dict
              (add-to-dict
               (create-dict {:key 666 :value 100})
               {:key 111 :value 1500})
              {:key 1000 :value 1000})
             {:key 2000 :value -1})))))

(deftest pass-string-as-key
  (is
   (=
    {:left {:left nil, :right nil, :key "Another String", :value -100}, :right nil, :key "he he, I am string!", :value 100}
    (add-to-dict (create-dict {:key "he he, I am string!" :value 100}) {:key "Another String" :value -100}))))

; is-like tests
(create-dict-test)
(create-nil-dict-test)
(add-to-nil-test)
(add-to-dict-test)
(add-nil-to-dict-test)
(add-existing-value-test)
(get-value-test)
(get-non-existing-value-test)
(simple-height-test)
(bidirectional-tree-height-test)
(bidirectional-tree-height-3-test)


; strange tests

(pass-string-as-key)