(ns odm.condition-def
  "3.1.1.3.11 - ConditionDef

  A condition-def defines a boolean condition. The description must be provided
  and should include a prose description. This is the normative content of the
  condition-def. The condition-def is referenced by the
  collection-exception-condition-oid attribute within a study metadata
  component which may be omitted under circumstances defined by the condition,
  i.e., when the formal-expression evaluates to true."
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.alias]
            [odm.common]
            [odm.data-formats :as df]
            [odm.description]
            [odm.formal-expression]
            [odm-spec.util :as u]))

(s/def ::oid
  (u/oid-spec "C"))

(s/def ::name
  ::df/name)

(s/def :odm/condition-def
  (s/keys :req [::oid ::name :odm/description]
          :opt [:odm/formal-expressions :odm/aliases]))
