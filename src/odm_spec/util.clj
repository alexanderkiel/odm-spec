(ns odm-spec.util
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as gen]
            [odm.data-formats :as df]))

(defn key-consistent? [id-attr]
  (fn [[k v]]
    (= k (id-attr v))))

(defn oid-spec
  "Returns a spec for OID's generating samples starting with prefix and ending
  with zero-padded numbers from 1 to 99 inclusive."
  [prefix]
  (let [oid (partial format (str prefix "%02d"))]
    (s/with-gen ::df/oid #(gen/fmap oid (gen/choose 1 99)))))

(defn distinct-oids? [oid coll]
  (= (count (into #{} (map oid) coll))
     (count coll)))

(defn distinct-oid-repeat-key-pairs? [oid repeat-key coll]
  (= (count (into #{} (map #(vector (oid %) (repeat-key %))) coll))
     (count coll)))

(defn distinct-order-numbers? [coll]
  (let [non-nil-order-numbers (comp (map :odm/order-number) (filter some?))]
    (= (count (into #{} non-nil-order-numbers coll))
       (count (into [] non-nil-order-numbers coll)))))
