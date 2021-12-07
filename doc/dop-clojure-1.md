```clojure
(defn take-nums
  [x]
  (take x (filter #(zero? (min (mod % 3) (mod % 5))) (range)))
  )

(println (apply + (take-nums 10)))
```

```clojure
(defn multiple-of-any? [coll n]
  (some #(zero? (rem n %)) coll))

(defn another-one
  [nums x]
  (
    ->> (range)
        (filter (partial multiple-of-any? nums))
        (take x)
        (reduce +))
  )


(println (another-one [3 5] 10))
```

```clojure
(defn another-one
  [nums]
  (
    ->> (range)
        (filter (partial multiple-of-any? nums))
        )
  )

(println (take 10 (another-one [3 5])))
```

