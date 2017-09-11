(ns odm.item-group-data
  "3.1.4.1.1.1.1 - ItemGroupData"
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.common]
            [odm.data-formats :as df]
            [odm.item-data]
            [odm.item-group-def :as ig]
            [odm-spec.util :as u]))

(s/def ::item-group-oid
  ::ig/oid)

(s/def ::item-group-repeat-key
  ::df/repeat-key)

(s/def ::item-data
  (s/and (s/coll-of :odm/item-data :gen-max 2)
         (partial u/distinct-values? :odm.item-data/item-oid)))

(s/def :odm/item-group-data
  (s/keys :req [::item-group-oid]
          :opt [::item-group-repeat-key :odm/tx-type ::item-data]))
