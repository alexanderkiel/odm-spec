(ns odm.method-def
  "3.1.1.3.9 - MethodDef

  A method definition defines how a data value can be obtained from a collection
  of other data values. The description element must be provided and should
  include a prose description. This is the normative content of the method
  definition.

  If a formal expression is provided, it must contain a machine-readable
  expression that implements the description and will return a value. Multiple
  formal expressions can be provided if each has a different context attribute,
  allowing the same expression to be represented in forms appropriate to
  multiple systems."
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.alias]
            [odm.common]
            [odm.data-formats :as df]
            [odm.description]
            [odm.formal-expression]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "M"))

(s/def ::name
  ::df/name)

(s/def ::type
  #{:computation :imputation :transpose :other})

(s/def :odm/method-def
  (s/keys :req [::oid ::name :odm/description]
          :opt [:odm/formal-expressions :odm/aliases]))
