(ns odm.item-ref
  "3.1.1.3.5.1 - ItemRef"
  (:require [clojure.spec :as s]
            [odm]
            [odm.condition-def :as c]
            [odm.item-def :as i]
            [odm.ref]))

(s/def ::item-oid
  ::i/oid)

(s/def :odm/item-ref
  (s/keys :req [::item-oid :odm/mandatory]
          :opt [:odm/order-number :odm.ref/collection-exception-condition-oid]))
