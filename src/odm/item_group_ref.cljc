(ns odm.item-group-ref
  "3.1.1.3.3.1 - ItemGroupRef"
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.common]
            [odm.condition-def :as c]
            [odm.item-group-def :as ig]
            [odm.ref]))

(s/def ::item-group-oid
  ::ig/oid)

(s/def :odm/item-group-ref
  (s/keys :req [::item-group-oid :odm/mandatory]
          :opt [:odm/order-number :odm.ref/collection-exception-condition-oid]))
