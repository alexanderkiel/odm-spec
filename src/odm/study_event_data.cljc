(ns odm.study-event-data
  "3.1.4.1.1 - StudyEventData

  Clinical data for a study event (visit). The model supports repeating study
  events (for example, when the same set of information is collected for a
  series of patient visits).

  The ::study-event-oid and ::study-event-repeat-key are used together to
  identify a particular study event. This pair of values uniquely identifies a
  study event within the containing subject. The ::study-event-repeat-key is
  present if and only if the study event definition is repeating."
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.common]
            [odm.data-formats :as df]
            [odm.form-data :as fd]
            [odm.study-event-def :as se]
            [odm-spec.util :as u]))

(s/def ::study-event-oid
  ::se/oid)

(s/def ::study-event-repeat-key
  ::df/repeat-key)

(s/def ::form-data
  (s/and (s/coll-of :odm/form-data :gen-max 2)
         (partial u/distinct-oid-repeat-key-pairs? ::fd/form-oid
                  ::fd/form-repeat-key)))

(s/def :odm/study-event-data
  (s/keys :req [::study-event-oid]
          :opt [::study-event-repeat-key :odm/tx-type ::form-data]))
