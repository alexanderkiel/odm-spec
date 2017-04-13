(ns odm.common
  "Common ODM Concepts."
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.data-formats :as df]))

(s/def :odm/mandatory
  boolean?)

(s/def :odm/order-number
  ::df/integer)

(s/def :odm/tx-type
  df/tx-type?)
