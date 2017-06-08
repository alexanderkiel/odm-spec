(ns odm.code-list
  "3.1.1.3.7 - CodeList

  Defines a discrete set of permitted values for an item. The definition can be
  an explicit list of values or a reference to an externally defined codelist."
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.alias]
            [odm.code-list-item :as code-list-item]
            [odm.common]
            [odm.data-formats :as df]
            [odm.description]
            [odm.enumerated-item :as enumerated-item]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "CL"))

(s/def ::name
  ::df/name)

;; The data type restricts the values that can appear in the code list whether
;; internal or external.
(s/def ::data-type
  #{:integer :float :text :string})

(s/def ::code-list-items
  (s/and (s/coll-of :odm/code-list-item :gen-max 2)
         #(u/distinct-values? ::code-list-item/coded-value %)
         #(u/distinct-or-no-values? ::code-list-item/rank %)
         #(u/distinct-or-no-values? :odm/order-number %)))

(s/def ::enumerated-items
  (s/and (s/coll-of :odm/enumerated-item :gen-max 2)
         #(u/distinct-values? ::enumerated-item/coded-value %)
         #(u/distinct-or-no-values? ::enumerated-item/rank %)
         #(u/distinct-or-no-values? :odm/order-number %)))

(s/def :odm/code-list
  (s/with-gen
    (s/and (s/keys :req [::oid ::name ::data-type
                         (or ::code-list-items ::enumerated-items)]
                   :opt [:odm/description :odm/aliases])
           #(not (and (contains? % ::code-list-items)
                      (contains? % ::enumerated-items))))
    #(s/gen (s/keys :req [::oid ::name ::data-type ::code-list-items]
                    :opt [:odm/description :odm/aliases]))))
