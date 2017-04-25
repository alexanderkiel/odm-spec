(ns odm.measurement-unit-ref
  "3.1.1.3.6.3 - MeasurementUnitRef"
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.measurement-unit :as mu]))

(s/def ::measurement-unit-oid
  ::mu/oid)

(s/def :odm/measurement-unit-ref
  (s/keys :req [::measurement-unit-oid]))
