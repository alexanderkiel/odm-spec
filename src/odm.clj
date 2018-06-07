(ns odm
  (:require
    [clojure.spec.alpha :as s]
    [clojure.string :as str]))

;; ---- Common ----------------------------------------------------------------

(s/def ::non-blank-string? (s/and string? (complement str/blank?)))

;; unbounded integer, same as xs:integer
(s/def ::integer integer?)

;; decimal number with arbitraty precision, same as xs:decimal
(s/def ::float decimal?)

;; a instant in time, same as xs:dateTime
(s/def ::date-time inst?)

(s/def ::oid
  (s/with-gen
    (s/conformer #(s/conform ::non-blank-string? (when (string? %) (str/trim %))))
    #(s/gen ::non-blank-string?)))

(s/def ::subject-key ::non-blank-string?)

(s/def ::repeat-key ::non-blank-string?)

(s/def ::name ::non-blank-string?)

(s/def ::tx-type
  #{:insert :update :remove :upsert :context})

;; ---- ODM -------------------------------------------------------------------

(s/def ::file-type #{:snapshot :transactional})

(s/def ::file-oid ::oid)

(s/def ::creation-date-time ::date-time)

;; ---- Study -----------------------------------------------------------------

(s/def ::study-oid ::oid)

;; ---- Study Event -----------------------------------------------------------

(s/def ::study-event-oid ::oid)

;; ---- Form Def --------------------------------------------------------------

(s/def ::form-def-oid ::oid)

;; ---- Item Group Def --------------------------------------------------------

(s/def ::item-group-def-oid ::oid)

;; ---- Item Def --------------------------------------------------------------

(s/def ::item-def-oid ::oid)
