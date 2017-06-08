(ns odm-spec.util
  (:require
    #?@(:clj
        [[clojure.spec :as s]
         [clojure.spec.gen :as gen]]
        :cljs
        [[cljs.spec :as s]
         [cljs.spec.impl.gen :as gen]
         [goog.string :refer [format]]
         [goog.string.format]])
         [odm.data-formats :as df]))

(defn oid-spec
  "Returns a spec for OID's generating samples starting with prefix and ending
  with zero-padded numbers from 1 to 99 inclusive."
  [prefix]
  (let [oid (partial format (str prefix "%02d"))]
    (s/with-gen ::df/oid #(gen/fmap oid (gen/choose 1 99)))))

(defn distinct-values?
  "Returns true if all values of the given key of maps in coll are distinct and
  not nil."
  [key coll]
  (= (count (into #{} (comp (remove nil?) (map key)) coll))
     (count coll)))

(defn distinct-or-no-values?
  "Returns true if all values of the given key of maps in coll are distinct and
  not nil."
  [key coll]
  (or (every? nil? (map key coll)) (distinct-values? key coll)))

(defn distinct-oid-repeat-key-pairs? [oid repeat-key coll]
  (= (count (into #{} (map #(vector (oid %) (repeat-key %))) coll))
     (count coll)))

(defn distinct-order-numbers? [coll]
  (let [non-nil-order-numbers (comp (map :odm/order-number) (filter some?))]
    (= (count (into #{} non-nil-order-numbers coll))
       (count (into [] non-nil-order-numbers coll)))))

#?(:clj
   (defmacro item-data-spec*
     "Spec has to be available under the alias `s`."
     [value-spec & opts]
     `(~'s/keys
        :req [:odm.item-data/item-oid :odm.item-data/data-type ~value-spec]
        :opt [:odm/tx-type ~@opts])))
