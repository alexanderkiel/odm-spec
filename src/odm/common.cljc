(ns odm.common
  "Common ODM Concepts."
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.data-formats :as df]))

(s/def :odm/mandatory
  boolean?)

(s/def :odm/order-number
  ::df/integer)

(s/def :odm/tx-type
  df/tx-type?)
