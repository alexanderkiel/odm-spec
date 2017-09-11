(ns odm.subject-data
  "3.1.4.1 - SubjectData

  Clinical data for a single subject."
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.common]
            [odm.data-formats :as df]
            [odm.study-event-data :as sed]
            [odm-spec.util :as u]))

;; A unique identifier of the subject (participant) within the study of the
;; surrounding clinical data map.
(s/def ::subject-key
  ::df/subject-key)

(s/def ::study-event-data
  (s/and (s/coll-of :odm/study-event-data :gen-max 2)
         (partial u/distinct-oid-repeat-key-pairs? ::sed/study-event-oid
                  ::sed/study-event-repeat-key)))

(s/def :odm/subject-data
  (s/keys :req [::subject-key]
          :opt [:odm/tx-type ::study-event-data]))
