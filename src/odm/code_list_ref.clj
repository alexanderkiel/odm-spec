(ns odm.code-list-ref
  "3.1.1.3.6.5 - CodeListRef"
  (:require [clojure.spec :as s]
            [odm.code-list :as cl]))

(s/def ::code-list-oid
  ::cl/oid)

(s/def :odm/code-list-ref
  (s/keys :req [::code-list-oid]))