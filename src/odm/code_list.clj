(ns odm.code-list
  "3.1.1.3.7 - CodeList

  Defines a discrete set of permitted values for an item. The definition can be
  an explicit list of values or a reference to an externally defined codelist."
  (:require [clojure.spec :as s]
            [odm]
            [odm.alias]
            [odm.data-formats :as df]
            [odm.description]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "CL"))

(s/def ::name
  ::df/name)

;; The data type restricts the values that can appear in the code list whether
;; internal or external.
(s/def ::data-type
  #{:integer :float :text :string})

;; Value of the codelist item (as it would occur in clinical data).
(s/def ::coded-value
  ::df/text)

;; Numeric significance of the CodeListItem relative to others in the CodeList.
;; Rank is optional, but if given for any CodeListItems in a CodeList it must
;; be given for all.
(s/def ::rank
  ::df/float)

(s/def ::decode
  ::df/translated-text)

(s/def :odm/code-list-item
  (s/keys :req [::coded-value ::decode]
          :opt [::rank :odm/order-number :odm/aliases]))

(defn distinct-coded-values? [coll]
  (= (count (into #{} (map ::coded-value) coll))
     (count coll)))

(s/def ::code-list-items
  (s/and (s/coll-of :odm/code-list-item :min-count 1)
         distinct-coded-values?))

(s/def :odm/enumerated-item
  (s/keys :req [::coded-value]
          :opt [::rank :odm/order-number :odm/aliases]))

(s/def ::enumerated-items
  (s/and (s/coll-of :odm/enumerated-item :min-count 1)
         distinct-coded-values?))

(s/def :odm/code-list
  (s/with-gen
    (s/and (s/keys :req [::oid ::name ::data-type
                         (or ::code-list-items ::enumerated-items)]
                   :opt [:odm/description :odm/aliases])
           #(not (and (contains? % ::code-list-items)
                      (contains? % ::enumerated-items))))
    #(s/gen (s/keys :req [::oid ::name ::data-type ::code-list-items]
                    :opt [:odm/description :odm/aliases]))))
