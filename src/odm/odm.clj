(ns odm.odm
  (:require [clojure.spec :as s]
            [odm.util :as u]))

(s/def ::file-type #{:snapshot :transactional})

(s/def ::file-oid ::u/oid)

(s/def ::creation-date-time inst?)
