(ns odm.file
  "3.1 - ODM"
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.clinical-data]
            [odm.data-formats :as df]
            [odm.reference-data]
            [odm.source-system :as ss]
            [odm.study]
            [odm-spec.util :as u]))

(s/def ::description
  ::df/text)

(s/def ::type
  #{:snapshot :transactional})

(s/def ::granularity
  #{:all :metadata :admin-data :reference-data :all-clinical-data
    :single-site :single-subject})

(s/def ::archival
  boolean?)

;; Unique identifier of the file.
(s/def ::oid
  (u/oid-spec "FI"))

(s/def ::creation-date-time
  ::df/date-time)

;; Identifier of the previous file in a series.
(s/def ::prior-oid
  ::oid)

;; Date-time at which the source database was queried
;; in order to create the file
(s/def ::as-of-date-time
  ::df/date-time)

;; The version of the ODM standard used.
(s/def ::odm-version
  #{"1.2" "1.2.1" "1.3" "1.3.1" "1.3.2"})

;; The organization that generated the ODM file.
(s/def ::originator
  ::df/text)

;; The computer system or database management system that is the source of
;; the information in this file.
(s/def ::source-system
  (s/keys :req [::ss/name ::ss/version]))

(s/def ::studies
  (s/coll-of :odm/study :gen-max 2))

;; A map of study OID to a map of metadata version OID to reference data.
(s/def ::reference-data
  (s/coll-of :odm/reference-data :gen-max 2))

;; A map of study OID to a map of metadata version OID to clinical data.
(s/def ::clinical-data
  (s/coll-of :odm/clinical-data :gen-max 2))

(s/def :odm/file
  (s/keys :req [::oid ::type ::creation-date-time]
          :opt [::studies ::reference-data ::clinical-data ::archival
                ::description ::granularity ::prior-oid ::originator
                ::as-of-date-time ::odm-version ::source-system]))
