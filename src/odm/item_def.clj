(ns odm.item-def
  (:require [clojure.spec :as s]
            [odm.util :as u]))

(s/def ::oid ::u/oid)
