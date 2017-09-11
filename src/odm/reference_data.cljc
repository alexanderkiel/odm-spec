(ns odm.reference-data
  "3.1.3 - ReferenceData

  Reference data provides information on how to interpret clinical data. For
  example, reference data might include lab normal ranges."
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.data-formats :as df]
            [odm.item-group-data :as igd]
            [odm.metadata-version]
            [odm.study]
            [odm-spec.util :as u]))

;; References the study that uses the data of the clinical data map.
(s/def ::study-oid
  :odm.study/oid)

;; References the metadata version (within the study) that governs the data of
;; the clinical data map.
(s/def ::metadata-version-oid
  :odm.metadata-version/oid)

(s/def ::item-group-data
  (s/and (s/coll-of :odm/item-group-data :gen-max 2)
         (partial u/distinct-oid-repeat-key-pairs? ::igd/item-group-oid
                  ::igd/item-group-repeat-key)))

(s/def :odm/reference-data
  (s/keys :req [::study-oid ::metadata-version-oid]))
