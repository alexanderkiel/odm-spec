(ns odm.ref
  (:require [clojure.spec :as s]
            [odm.condition-def :as c]))

(s/def ::collection-exception-condition-oid
  ::c/oid)


