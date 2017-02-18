(ns odm.item-def
  "3.1.1.3.6 - ItemDef

  An ItemDef describes a type of item that can occur within a study. Item
  properties include name, datatype, measurement units, range or codelist
  restrictions, and several other properties."
  (:require [clojure.spec :as s]
            [odm]
            [odm.code-list-ref]
            [odm.data-formats :as df]
            [odm.def]
            [odm.description]
            [odm.measurement-unit-ref]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "I"))

(s/def ::name
  ::df/name)

(s/def ::data-type
  #{:text :integer :float :date :time :date-time :string :boolean :double
    :hex-binary :base64-binary :hex-float :base64-float :partial-date
    :partial-time :partial-date-time :duration-date-time :interval-datetime
    :incomplete-date-time :incomplete-date :incomplete-time :uri})

;; The length attribute is required when data type is text or string, optional
;; when data type is integer or float, and should not be given for the other
;; datatypes.
(s/def ::length
  ::df/positive-integer)

(s/def ::significant-digits
  ::df/non-negative-integer)

;; The SDS var name, origin and comment attributes carry submission
;; information as described in the latest version of CDISC SDTM.
(s/def ::sds-var-name
  ::df/sas-name)

;; The question contains the text shown to a human user when prompted to
;; provide data for this item.
(s/def ::question
  ::df/translated-text)

;; The MeasurementUnitRefs list the acceptable measurement units for this type
;; of item. Only numeric items can have measurement units. If only one
;; MeasurementUnitRef is present, all items of this type carry this measurement
;; unit by default, i.e. if a MeasurementUnitRef is defined on the ItemDef, and
;; no MeasurementUnitRef is given on the corresponding ItemData, the value given
;; by the ItemData has the units given by the ItemDef-MeasurementUnitRef.
;; If no MeasurementUnitRef is present on the definition of a numeric Item, the
;; Item's value is scalar (i.e., a pure number).
(s/def ::measurement-unit-refs
  (s/and (s/coll-of :odm/measurement-unit-ref)
         (partial u/distinct-oids? :odm.measurement-unit-ref/measurement-unit-oid)))

(defn- length-given-on-text-or-string? [{:keys [::data-type ::length]}]
  (or (not (#{:text :string} data-type)) length))

(defn- length-and-significant-digits-given-or-absent-on-float?
  [{:keys [::data-type ::length ::significant-digits]}]
  (or (not (#{:float} data-type))
      (and length significant-digits)
      (and (not length) (not significant-digits))))

(s/def :odm/item-def
  (s/and (s/keys :req [::oid ::name ::data-type]
                 :opt [::length ::significant-digits
                       ::sds-var-name :odm.def/origin :odm.def/comment
                       :odm/description ::question ::measurement-unit-refs
                       :odm/code-list-ref :odm/aliases])
         length-given-on-text-or-string?
         length-and-significant-digits-given-or-absent-on-float?))
