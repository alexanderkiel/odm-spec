(ns odm.form-def
  "3.1.1.3.4 - FormDef"
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.alias]
            [odm.common]
            [odm.data-formats :as df]
            [odm.def]
            [odm.description]
            [odm.item-group-ref]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "F"))

(s/def ::name
  ::df/name)

(s/def ::item-group-refs
  (s/and (s/coll-of :odm/item-group-ref :gen-max 2)
         (partial u/distinct-values? :odm.item-group-ref/item-group-oid)
         #(u/distinct-order-numbers? %)))

(s/def :odm/form-def
  (s/keys :req [::oid ::name :odm.def/repeating]
          :opt [:odm/description ::item-group-refs :odm/aliases]))
