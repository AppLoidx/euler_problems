(ns tree.Dictionary)



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
    (hash (:key node))))

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


(defn main
  []

  (println
   (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}))
  (println
   (remove-node
    (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) 666)))



(main)






