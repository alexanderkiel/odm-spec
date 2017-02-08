(ns odm.source-system
  "3.1 ODM - SourceSystem Attribute"
  (:require [clojure.spec :as s]
            [odm.data-formats :as df]))

;; The computer system or database management system that is the source of
;; the information in this file.
(s/def ::name
  ::df/text)

;; The version of the source system.
(s/def ::version
  ::df/text)
