(ns odm
  (:require [clojure.spec.alpha :as s]
            [odm.form-def]
            [odm.item-def]
            [odm.item-group-def]
            [odm.odm]
            [odm.study]
            [odm.study-event]
            [odm.util :as u]))

;; unbounded integer, same as xs:integer
(s/def ::integer integer?)

;; decimal number with arbitraty precision, same as xs:decimal
(s/def ::float decimal?)

;; a instant in time, same as xs:dateTime
(s/def ::date-time inst?)

(s/def ::oid
  ::u/oid)

(s/def ::subject-key ::u/non-blank-string?)

(s/def ::repeat-key ::u/non-blank-string?)

(s/def ::name ::u/non-blank-string?)

(s/def ::tx-type
  #{:insert :update :remove :upsert :context})

(s/def ::data-type
  u/data-types)
