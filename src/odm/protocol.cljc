(ns odm.protocol
  "3.1.1.3.2 - Protocol

  A protocol lists the kinds of study events that can occur within a specific
  version of a study. All clinical data must occur within one of these study
  events.

  Note: A study whose metadata does not contain a protocol definition cannot
  have any clinical data. Such studies can serve as \"common metadata
  dictionaries\" -- allowing sharing of metadata across studies."
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.alias]
            [odm.common]
            [odm.data-formats :as df]
            [odm.description]
            [odm.study-event-ref]
            [odm-spec.util :as u]))

(s/def ::study-event-refs
  (s/and (s/coll-of :odm/study-event-ref :gen-max 2)
         (partial u/distinct-oids? :odm.study-event-ref/study-event-oid)
         #(u/distinct-order-numbers? %)))

(s/def :odm/protocol
  (s/keys :opt [:odm/description ::study-event-refs :odm/aliases]))
