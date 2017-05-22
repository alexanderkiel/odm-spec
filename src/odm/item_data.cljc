(ns odm.item-data
  "3.1.4.1.1.1.1.2 - ItemData"
  (:require
    #?@(:clj  [[clojure.spec :as s]
               [odm-spec.util :refer [item-data-spec*]]]
        :cljs [[cljs.spec :as s]
               [odm-spec.util :refer-macros [item-data-spec*]]])
               [odm.common]
               [odm.data-formats :as df]
               [odm.item-def :as i]
               [odm.measurement-unit :as mu]))

(s/def ::item-oid
  ::i/oid)

(s/def ::data-type
  ::i/data-type)

;; Only numeric items can have measurement units.
(s/def ::measurement-unit-oid
  ::mu/oid)

(s/def ::string-value
  ::df/string)

(s/def ::integer-value
  ::df/integer)

(s/def ::float-value
  ::df/float)

(s/def ::date-time-value
  ::df/date-time)

(s/def ::boolean-value
  boolean?)

(defmulti item-data-spec ::data-type)

(defmethod item-data-spec :string [_]
  (item-data-spec* ::string-value))

(defmethod item-data-spec :integer [_]
  (item-data-spec* ::integer-value ::measurement-unit-oid))

(defmethod item-data-spec :float [_]
  (item-data-spec* ::float-value ::measurement-unit-oid))

(defmethod item-data-spec :date-time [_]
  (item-data-spec* ::date-time-value))

(defmethod item-data-spec :boolean [_]
  (item-data-spec* ::boolean-value))

(s/def :odm/item-data
  (s/multi-spec item-data-spec ::data-type))
