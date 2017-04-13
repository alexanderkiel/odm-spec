(ns odm.study-event-ref
  "3.1.1.3.2.2 - StudyEventRef"
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.common]
            [odm.ref]
            [odm.study-event-def :as se]))

(s/def ::study-event-oid
  ::se/oid)

(s/def :odm/study-event-ref
  (s/keys :req [::study-event-oid :odm/mandatory]
          :opt [:odm/order-number :odm.ref/collection-exception-condition-oid]))
