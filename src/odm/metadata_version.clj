(ns odm.metadata-version
  "3.1.1.3 - MetaDataVersion"
  (:require [clojure.spec :as s]
            [odm.code-list :as cl]
            [odm.condition-def :as c]
            [odm.data-formats :as df]
            [odm.include]
            [odm.method-def :as m]
            [odm.study-event-def :as se]
            [odm.form-def :as f]
            [odm.item-group-def :as ig]
            [odm.item-def :as i]
            [odm.protocol]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "V"))

(s/def ::name
  ::df/name)

(s/def ::description
  ::df/text)

(s/def ::study-event-defs
  (s/and (s/coll-of :odm/study-event-def :gen-max 1)
         (partial u/distinct-oids? ::se/oid)))

(s/def ::form-defs
  (s/and (s/coll-of :odm/form-def :gen-max 1)
         (partial u/distinct-oids? ::f/oid)))

(s/def ::item-group-defs
  (s/and (s/coll-of :odm/item-group-def :gen-max 1)
         (partial u/distinct-oids? ::ig/oid)))

(s/def ::item-defs
  (s/and (s/coll-of :odm/item-def :gen-max 1)
         (partial u/distinct-oids? ::i/oid)))

(s/def ::code-lists
  (s/and (s/coll-of :odm/code-list :gen-max 1)
         (partial u/distinct-oids? ::cl/oid)))

(s/def ::condition-defs
  (s/and (s/coll-of :odm/condition-def :gen-max 1)
         (partial u/distinct-oids? ::c/oid)))

(s/def ::method-defs
  (s/and (s/coll-of :odm/method-def :gen-max 1)
         (partial u/distinct-oids? ::m/oid)))

(s/def :odm/metadata-version
  (s/keys :req [::oid ::name]
          :opt [::description
                :odm/include
                :odm/protocol
                ::study-event-defs
                ::form-defs
                ::item-group-defs
                ::item-defs
                ::code-lists
                ::condition-defs
                ::method-defs]))
