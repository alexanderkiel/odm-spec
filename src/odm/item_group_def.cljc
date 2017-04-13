(ns odm.item-group-def
  "3.1.1.3.5 - ItemGroupDef"
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.alias]
            [odm.common]
            [odm.data-formats :as df]
            [odm.def]
            [odm.description]
            [odm.item-ref]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "IG"))

(s/def ::name
  ::df/name)

(s/def ::reference-data?
  boolean?)

(s/def ::item-refs
  (s/and (s/coll-of :odm/item-ref :gen-max 2)
         (partial u/distinct-oids? :odm.item-ref/item-oid)
         #(u/distinct-order-numbers? %)))

(s/def :odm/item-group-def
  (s/keys :req [::oid ::name :odm.def/repeating]
          :opt [::reference-data? :odm.def/domain :odm.def/origin
                :odm.def/purpose :odm.def/comment :odm/description
                ::item-refs :odm/aliases]))
