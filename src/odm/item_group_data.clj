(ns odm.item-group-data
  "3.1.4.1.1.1.1 - ItemGroupData"
  (:require [clojure.spec :as s]
            [odm]
            [odm.data-formats :as df]
            [odm.item-data]
            [odm.item-group-def :as ig]
            [odm-spec.util :as u]))

(s/def ::item-group-oid
  ::ig/oid)

(s/def ::item-group-repeat-key
  ::df/repeat-key)

(s/def ::item-data
  (s/and (s/coll-of :odm/item-data :min-count 1)
         (partial u/distinct-oids? :odm.item-data/item-oid)))

(s/def :odm/item-group-data
  (s/keys :req [::item-group-oid]
          :opt [::item-group-repeat-key :odm/tx-type ::item-data]))