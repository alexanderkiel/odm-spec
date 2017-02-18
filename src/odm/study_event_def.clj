(ns odm.study-event-def
  "3.1.1.3.3 - StudyEventDef

  A StudyEventDef packages a set of forms."
  (:require [clojure.spec :as s]
            [odm]
            [odm.alias]
            [odm.data-formats :as df]
            [odm.def]
            [odm.description]
            [odm.form-ref]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "SE"))

(s/def ::name
  ::df/name)

(s/def ::type
  #{
    ;; Scheduled study events correspond to sets of forms that are expected to
    ;; be collected for each subject as part of the planned visit sequence for
    ;; the study.
    :scheduled

    ;; Unscheduled study events are designed to collect data that may or may
    ;; not occur for any particular subject such as a set of forms that are
    ;; completed for an early termination due to a serious adverse event.
    :unscheduled

    ;; A common study event is a collection of forms that are used at several
    ;; different data collection events such as an adverse event or concomitant
    ;; medications log.
    :common})

;; The category attribute is typically used to indicate the study phase
;; appropriate to this type of study event. Examples might include screening,
;; pre-treatment, treatment and follow-up.
(s/def ::category
  ::df/text)

(s/def ::form-refs
  (s/and (s/coll-of :odm/form-ref)
         (partial u/distinct-oids? :odm.form-ref/form-oid)
         #(u/distinct-order-numbers? %)))

(s/def :odm/study-event-def
  (s/keys :req [::oid ::name :odm.def/repeating ::type]
          :opt [::category :odm/description ::form-refs :odm/aliases]))
