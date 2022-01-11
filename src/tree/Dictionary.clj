(ns tree.Dictionary)

; left - less, right - greater

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

(defn key-wrapper
  [key]
  (if (instance? Number key) key (hash key)))

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
    (if (= (key-wrapper key) (nodekey-wrapper root))
      root
      (if (> (key-wrapper key) (nodekey-wrapper root))
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
     (if (= (key-wrapper key) (nodekey-wrapper root))
       {:parent parent :child root}
       (if (> (key-wrapper key) (nodekey-wrapper root))
         (find-node-parent (:right root) key root)
         (find-node-parent (:left root) key root)))))
  ([root key]
   (find-node-parent root key nil)))

(defn add-left-node
  [left-node root]
  (if (nil? root)
    left-node
    (assoc (find-smallest-node root) :left left-node)))

(defn remove-node-from-tree
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
  (remove-node-from-tree
   (find-node-parent root remove-key)))


;; (defn add-to-dict-v1
;;   ([dict entry]
;;    (if (nil? entry)
;;      dict
;;      (insert-node dict (make-node (:key entry) (:value entry)))))

  ;; ([entry]
  ;;  (insert-node nil (make-node (:key entry) (:value entry)))))

(defn entry-node
  [dict entry]

  (if (nil? entry)
    (dict)
    (insert-node dict {:key (key entry) :value (val entry) :left nil :right nil})))

(defn add-to-dict
  ([dict inits]
   (reduce entry-node dict inits))

  ([inits]
   (reduce entry-node nil inits)))

(defn value-from-dict
  [dict key]
  (:value (find-node dict key)))


(defn create-dict
  [& args]
  (apply add-to-dict args))


;; (defn plus
;;   [dict1 dict2]
;;   ())


(defn main
  []
  ;; (println
  ;;  (add-to-dict (create-dict {:goodnum 666 :another 100}) {:my 111 :not-my 1500}))
  ;; (println
  ;;  (add-to-dict (create-dict {:badnum 666 :coolnum 100}) {:notmyval 111 :sheeez 1500}))
  (println
   (remove-node
    (add-to-dict (create-dict {:key1 666 :key2 100}) {:key3 111 :key6 1500}) :key1)))




(main)






