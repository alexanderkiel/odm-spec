(ns odm.form-data
  "3.1.4.1.1.1 - FormData

  Clinical data for a form (page). The model supports repeating forms in a
  single study event (for example, when several adverse events are recorded in
  a single patient visit)."
  (:require [clojure.spec :as s]
            [odm]
            [odm.data-formats :as df]
            [odm.form-def :as f]
            [odm.item-group-data :as igd]
            [odm-spec.util :as u]))

(s/def ::form-oid
  ::f/oid)

(s/def ::form-repeat-key
  ::df/repeat-key)

(s/def ::item-group-data
  (s/and (s/coll-of :odm/item-group-data)
         (partial u/distinct-oid-repeat-key-pairs? ::igd/item-group-oid
                  ::igd/item-group-repeat-key)))

(s/def :odm/form-data
  (s/keys :req [::form-oid]
          :opt [:odm/tx-type ::form-repeat-key ::item-group-data]))
