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

(defn operat
  [obj]
  (cond (instance? Number obj) +
        :else concat))

(defn insert-node
  "inserts a node into a BST"
  [root new-node]
  (if (nil? root)
    new-node
    (if (right-than new-node root)
      (assoc root :right (insert-node (:right root) new-node))
      ;; (println root)
      (if (equals new-node root)
        (assoc root :value ((operat (:value new-node))
                            (:value new-node) (:value root)))
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
     (let [target (key-wrapper key) node (nodekey-wrapper root)]
       (cond
         (= target node) {:parent parent :child root}
         (> target node) (find-node-parent (:right root) key root)
         :else (find-node-parent (:left root) key root)))))
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
  ([root key]
   (remove-node-from-tree
    (find-node-parent root key)))
  ([root key & keys]
   (reduce remove-node (remove-node root key) keys)))

(defn entry-node
  ([dict entry]

   (if (nil? entry)
     dict
     (if (map-entry? dict)
       (insert-node {:key (key dict) :value (val dict) :left nil :right nil} {:key (key entry) :value (val entry) :left nil :right nil})
       (insert-node dict {:key (key entry) :value (val entry) :left nil :right nil}))))
  ([entry]
   (entry-node nil entry)))

(defn add-to-dict
  ([dict init & inits_var]
   (reduce add-to-dict dict (merge inits_var init)))

  ([dict inits]
   (reduce entry-node dict inits))
  ([inits]
   (reduce entry-node inits)))

(defn value-from-dict
  [dict key]
  (:value (find-node dict key)))

(defn create-dict
  ([& args]
   (reduce add-to-dict nil (merge args))))

(defn dvalues
  ([root seq]

   (if (nil? (:left root))

     (if (nil? (:right root))
       (concat seq (sequence [(array-map (keyword (:key root)) (:value root))]))
       (dvalues (:right root) (concat seq (sequence [(array-map (keyword (:key root)) (:value root))]))))
     (dvalues (:left root) (concat seq (sequence [(array-map (keyword (:key root)) (:value root))])))))
  ([root]
   (dvalues root (sequence []))))

(defn dfilter

  ([f root]
   (filter f (dvalues root))))

(defn dmap
  ([f root]
   (reduce add-to-dict nil (map f (dvalues root)))))

(defn entry-node-reducer
  [acc entry]
  (entry-node acc entry))

(defn dmerge
  ([dict1 dict2]
   (reduce entry-node-reducer nil (merge-with + (map first (concat (dvalues dict1) (dvalues

                                                                                    dict2)))))))






;; (main)






