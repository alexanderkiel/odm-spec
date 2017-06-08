(ns odm.study
  "3.1.1 Study"
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.data-formats :as df]
            [odm.measurement-unit :as mu]
            [odm.metadata-version :as v]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "S"))

;; A short external name for the study.
(s/def ::name
  ::df/name)

;; A free-text description of the study.
(s/def ::description
  ::df/text)

;; The sponsor's internal name for the protocol.
(s/def ::protocol-name
  ::df/name)

(s/def ::measurement-units
  (s/and (s/coll-of :odm/measurement-unit :gen-max 2)
         (partial u/distinct-values? ::mu/oid)))

(s/def ::metadata-versions
  (s/and (s/coll-of :odm/metadata-version :gen-max 2)
         (partial u/distinct-values? ::v/oid)))

(s/def :odm/study
  (s/keys :req [::oid ::name ::description ::protocol-name]
          :opt [::measurement-units ::metadata-versions]))
