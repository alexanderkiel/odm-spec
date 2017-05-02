(ns odm.item-ref
  "3.1.1.3.5.1 - ItemRef"
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.common]
            [odm.code-list :as cl]
            [odm.data-formats :as df]
            [odm.item-def :as i]
            [odm.method-def :as m]
            [odm.ref]))

(s/def ::item-oid
  ::i/oid)

;; The KeySequence (if present) indicates that this item is a key for the
;; enclosing item group. It also provides an ordering for the keys.
(s/def ::key-sequence
  ::df/integer)

;; The MethodOID references a MethodDef used to derive the value of this item.
(s/def ::method-oid
  ::m/oid)

;; The Role attribute provides a single role name describing the use of this data item.
(s/def ::role
  ::df/text)

(s/def ::role-code-list-oid
  ::cl/oid)

(s/def :odm/item-ref
  (s/and (s/keys :req [::item-oid :odm/mandatory]
                 :opt [:odm/order-number ::key-sequence ::method-oid ::role
                       :odm.ref/collection-exception-condition-oid])
         #(if (::role-code-list-oid %) (::role %) true)))
