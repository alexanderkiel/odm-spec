(ns odm.formal-expression
  "3.1.1.3.11.1 - FormalExpression

  A formal-expression used within a condition-def or a range-check must evaluate
  to true or false. A formal-expression referenced within a method-def having
  type imputation, computation or transpose must evaluate to the correct
  data-type for an item that may be imputed or computed using the method."
  (:require [clojure.spec :as s]
            [odm]
            [odm.data-formats :as df]))

;; A free-form qualifier to suggest an appropriate computer language to be
;; used when evaluating the formal-expression content.
(s/def ::context
  ::df/text)

(s/def ::expression
  ::df/text)

(s/def :odm/formal-expression
  (s/keys :req [::context ::expression]))

(s/def :odm/formal-expressions
  (s/coll-of :odm/formal-expression :min-count 1))
