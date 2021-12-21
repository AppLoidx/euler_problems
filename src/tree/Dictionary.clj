(ns tree.Dictionary)

(use
 'clojure.test)

;left - less, right - greater

(defn make-node
  ([left right key value]
   {:left left :right right :key key :value value})
  ([key value]
   (make-node nil nil key value)))

(defn nodekey-wrapper
  [node]
  (if (instance? Number (:key node))
    (:key node)
    (hash (:key node)))
  )

(defn comparator-over-nodes
  "возвращает функцию компаратор между нодами"
  ([func]
   (fn [node1 node2]
     (func (nodekey-wrapper node1) (nodekey-wrapper node2))))
  ([func & args]
   (comparator-over-nodes #(func args %))))

(def right-than
  (comparator-over-nodes >))

(def equals
  (comparator-over-nodes =))

(defn height
  "Determines the depth of the BST"
  ([root count]
   (if root
     (max (height (:left root) (inc count))
          (height (:right root) (inc count)))
     count))
  ([root]
   (height root 0)))

(defn insert-node
  "inserts a node into a BST"
  [root new-node]
  (if (nil? root)
    new-node
    (if (right-than new-node root)
      (assoc root :right (insert-node (:right root) new-node))

      (if (equals new-node root)
        (assoc root :value (:value new-node))
        (assoc root :left (insert-node (:left root) new-node))))))

(defn find-node
  "find node in BST"
  [root key]
  (if (nil? (:key root))
    nil
    (if (= key (:key root))
      root
      (if (> key (:key root))
        (find-node (:right root) key)
        (find-node (:left root) key)))))

(defn find-smallest-node
  [root]
  (if (nil? (:left root))
    root
    (find-smallest-node (:left root))))


(defn find-node-parent
  "find parent-node in BST"
  ([root key parent]
   (if (nil? (:key root))
     nil
     (if (= key (:key root))
       {:parent parent :child root}
       (if (> key (:key root))
         (find-node-parent (:right root) key root)
         (find-node-parent (:left root) key root)))))
  ([root key]
   (find-node-parent root key nil)))

(defn add-left-node
  [left-node root]
  (if (nil? root)
    left-node
    (assoc (find-smallest-node root) :left left-node)))

(defn remove-child
  [e]
  ; {:parent parent :child root} object
  (if (nil? (:parent e))
    (if (nil? (:right (:child e)))
      (:left (:child e))
      (add-left-node (:left (:child e)) (:right (:child e))))
    (if (right-than (:child e) (:parent e))
      (assoc (:parent e) :right (add-left-node (:left (:child e)) (:right (:child e))))
      (assoc (:parent e) :left (add-left-node (:left (:child e)) (:right (:child e)))))))


(defn remove-node
  [root remove-key]
  (remove-child
   (find-node-parent root remove-key)))

(defn greatest
  [root]
  (if (:right root)
    (greatest (:right root))
    root))

(def my-tree
  (assoc
   (assoc
    (assoc (make-node 10 666)
           :left (make-node 8 666))
    :right (make-node 11 666))
   :left (make-node 4 666)))

(defn add-to-dict
  ([dict entry]
   (if (nil? entry)
     dict
     (insert-node dict (make-node (:key entry) (:value entry)))))

  ([entry]
   (insert-node nil (make-node (:key entry) (:value entry)))))

(defn value-from-dict
  [dict key]
  (:value (find-node dict key)))


(defn create-dict
  [& args]
  (apply add-to-dict args))


(defn -main
  [& args]

  (println
   (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}))
  (println
   (remove-node
    (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) 666)))


(def my-tree
  (assoc
   (make-node 666 100)
   :left (make-node 111 1600)))


;(-main)


; TEST ------------------------------------

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
     {:left nil, :right nil,
      :key  111, :value 1500},
     :right nil,
     :key   666,
     :value 100}
    (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}))))

(deftest add-to-nil-test
  (is
   (=
    {:left nil, :right nil,
     :key  666, :value 100}
    (add-to-dict nil {:key 666 :value 100}))))

(deftest add-nil-to-dict-test
  (is
   (=
    {:left nil, :right nil,
     :key  555, :value 0}
    (add-to-dict (create-dict {:key 555 :value 0}) nil))))

(deftest add-existing-value-test
  (is
   (=
    {:left
     {:left nil, :right nil,
      :key  111, :value 1500},
     :right nil,
     :key   666,
     :value -20}
    (add-to-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) {:key 666 :value -20})
     )))

(deftest get-value-test
  (is
   (=
    1500
    (value-from-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) 111)
     )))


(deftest get-non-existing-value-test
  (is
   (=
    nil
    (value-from-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) 0)
    )))

(deftest simple-height-test
  (is
   (=
    2
    (height (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}))
    )))

(deftest bidirectional-tree-height-test
  (is
   (=
    2
    (height (add-to-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) {:key 1000 :value 1000}))
    )))

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
             {:key 2000 :value -1}))
    )))

(deftest pass-string-as-key
  (is
   (
     =
     {:left {:left nil, :right nil, :key "Another String", :value -100}, :right nil, :key "he he, I am string!", :value 100}
     (add-to-dict (create-dict {:key "he he, I am string!" :value 100}) {:key "Another String" :value -100})

     )))

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


