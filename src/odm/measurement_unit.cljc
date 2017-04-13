(ns odm.measurement-unit
  "3.1.1.2.1   MeasurementUnit

  The physical unit of measure for a data item or value. The meaning of a
  measurement unit is determined by its name. Examples include
  kilograms, centimeters, cells/milliliter, etc.

  Only numeric items can have measurement units."
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.alias]
            [odm.data-formats :as df]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "U"))

;; The meaning of a measurement unit is determined by its name.
(s/def ::name
  ::df/text)

;; A human-readable name for a measurement unit.
(s/def ::symbol
  ::df/translated-text)

(s/def :odm/measurement-unit
  (s/keys :req [::oid ::name ::symbol]
          :opt [:odm/aliases]))
