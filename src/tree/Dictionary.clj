(ns tree.Dictionary)

(use
 'clojure.test)

;left - less, right - greater

(defn make-node
  ([left right key value]
   {:left left :right right :key key :value value})
  ([key value]
   (make-node nil nil key value)))

(defn comparator-over-nodes
  "Returns a comparison function specific for values of tree nodes"
  ([func]
   (fn [node1 node2]
     (func (:key node1) (:key node2))))
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


(deftest non-existing-value
  (is
   (=
    nil
    (value-from-dict (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) 0)
    )))

(create-dict-test)

(create-nil-dict-test)

(add-to-nil-test)

(add-to-dict-test)

(add-nil-to-dict-test)

(add-existing-value-test)

(get-value-test)

(non-existing-value)


