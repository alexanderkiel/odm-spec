(ns odm.item-ref
  "3.1.1.3.5.1 - ItemRef"
  (:require [clojure.spec :as s]
            [odm]
            [odm.code-list :as cl]
            [odm.data-formats :as df]
            [odm.item-def :as i]
            [odm.method-def :as m]
            [odm.ref]))

(s/def ::item-oid
  ::i/oid)

(s/def ::key-sequence
  ::df/integer)

(s/def ::method-oid
  ::m/oid)

(s/def ::role
  ::df/text)

(s/def ::role-code-list-oid
  ::cl/oid)

(s/def :odm/item-ref
  (s/and (s/keys :req [::item-oid :odm/mandatory]
                 :opt [:odm/order-number ::key-sequence ::method-oid ::role
                       :odm.ref/collection-exception-condition-oid])
         #(if (::role-code-list-oid %) (::role %) true)))
