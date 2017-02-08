(ns odm
  "Common ODM Concepts."
  (:require [clojure.spec :as s]
            [odm.data-formats :as df]))

(s/def ::mandatory
  boolean?)

(s/def ::order-number
  ::df/integer)

(s/def ::tx-type
  df/tx-type?)
