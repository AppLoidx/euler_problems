(ns tree.Dictionary)

(defn make-node
  ([less greater value payload]
   {:less less :greater greater :value value :payload payload})
  ([value payload]
   (make-node nil nil value payload)))

(defn comparator-over-nodes
  "Returns a comparison function specific for values of tree nodes"
  ([func]
   (fn [node1 node2]
     (func (:value node1) (:value node2))))
  ([func & args]
   (comparator-over-nodes #(func args %))))

(def equals
  (comparator-over-nodes =))

(def greater-than
  (comparator-over-nodes >))

(def less-than
  (comparator-over-nodes <))

(defn height
  "Determines the depth of the BST"
  ([root count]
   (if root
     (max (height (:less root) (inc count))
          (height (:greater root) (inc count)))
     count))
  ([root]
   (height root 0)))

(defn insert-node
  "inserts a node into a BST"
  [root new-node]
  (if (nil? root)
    new-node
    (if (greater-than new-node root)
      (assoc root :greater (insert-node (:greater root) new-node))

      (if (equals new-node root)
        (assoc root :payload (:payload new-node))
        (assoc root :less (insert-node (:less root) new-node)))

      )))

(defn find-node
  "find node in BST"
  [root key]
  (if (nil? (:value root))
    nil
    (if (= key (:value root))
      root
      (if (> key (:value root))
        (find-node (:greater root) key)
        (find-node (:less root) key)
        )
      )
    ))

(defn find-less-node
  "find node in BST"
  [root]
  (if (nil? (:less root))
    root
    (find-less-node (:less root))))


(defn find-node-parent
  "find parent-node in BST"
  ([root key parent]
   (if (nil? (:value root))
     nil
     (if (= key (:value root))
       {:parent parent :child root}
       (if (> key (:value root))
         (find-node-parent (:greater root) key root)
         (find-node-parent (:less root) key root)
         )
       )
     ))
  ([root key]
   (find-node-parent root key nil))
  )

(defn add-less-node
  [less-node root]
  (if (nil? root)
    less-node
    (assoc (find-less-node root) :less less-node)
    )

  )

(defn -remove-child
  [e]
  (if (nil? (:parent e))
    (if (nil? (:greater (:child e) ) )
      (:less (:child e) )
      (add-less-node (:less (:child e)) (:greater (:child e)))
      )
    (if (greater-than (:child e) (:parent e))
      (assoc (:parent e) :greater (add-less-node (:less (:child e)) (:greater (:child e) )))
      (assoc (:parent e) :less (add-less-node (:less (:child e)) (:greater (:child e) ))))
    ))


(defn remove-node
  [root remove-key]
  (-remove-child (
                   find-node-parent root remove-key))

  )



(defn bld-rand-tree
  "Creates a BST of depth target-height full of random floats"
  ([target-height]
   (bld-rand-tree target-height (make-node (rand) (rand))))
  ([target-height root]
   (if (>= target-height (height root))
     (bld-rand-tree target-height (insert-node root (make-node (rand) (rand))))
     root)))

(defn greatest
  [root]
  (if (:greater root)
    (greatest (:greater root))
    root))

(defn is-bst
  "Not only does this check to see if the value at each 'less' child of
  each node is less than the value at that node (and 'greater'), but it also
  checks that EVERY element to the 'less' of each node is less than that node.
  Otherwise, the tree is not a BST. Additionally, this function ensures that "
  ([root greaters lesss]
   (if root
     (every? identity
             [(if (every? (fn [a-min] (< (:value root) a-min)) lesss)
                (is-bst (:less root) greaters (conj lesss (:value root)))
                false)
              (if (every? (fn [a-max] (> (:value root) a-max)) greaters)
                (is-bst (:greater root) (conj greaters (:value root)) lesss)
                false)])
     true))
  ([root]
   (is-bst root [] [])))

(def my-tree
  (assoc (assoc (assoc (make-node 10 666)
                  :less (make-node 8 666))
           :greater (make-node 11 666))
    :less (make-node 4 666)))

(defn add-to-dict
  ([dict entry]
   (insert-node dict (make-node (:key entry) (:value entry))))

  ([entry]
   (insert-node nil (make-node (:key entry) (:value entry))))
  )

(defn value-from-dict
  [dict key]
  (:payload (find-node dict key))
  )


(defn create-dict
  [& args]
  (apply add-to-dict args)
  )


(
  defn -main
  "Demo this thing"
  [& args]
  ;(println (str "Is my-tree a BST? " (hip-speech (is-bst my-tree))))
  ;(println (str "Well what about rand-bst? " (hip-speech (is-bst rand-bst))))

  ;(println (insert-node nil (make-node 10 666)))
  (println (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}))
  (println (
             remove-node
             (add-to-dict (create-dict {:key 666 :value 100}) {:key 111 :value 1500}) 666)))

