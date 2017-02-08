(ns odm.clinical-data
  "3.1.4 - ClinicalData

  Clinical data is the data of subjects which was recored in a study under a
  particular metadata version."
  (:require [clojure.spec :as s]
            [odm.data-formats :as df]
            [odm.metadata-version]
            [odm.study]
            [odm.subject-data]
            [odm-spec.util :as u]))

;; References the study that uses the data of the clinical data map.
(s/def ::study-oid
  :odm.study/oid)

;; References the metadata version (within the study) that governs the data of
;; the clinical data map.
(s/def ::metadata-version-oid
  :odm.metadata-version/oid)

(s/def ::subject-data
  (s/coll-of :odm/subject-data :min-count 1))

(s/def :odm/clinical-data
  (s/keys :req [::study-oid ::metadata-version-oid]
          :opt [::subject-data]))
