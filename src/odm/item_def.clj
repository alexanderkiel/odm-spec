(ns odm.item-def
  (:require [clojure.spec.alpha :as s]
            [odm.util :as u]))

(s/def ::oid ::u/oid)

(s/def ::data-type
  u/data-types)
