(ns odm.include
  "3.1.1.3.1 - Include"
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.data-formats :as df]))

(s/def ::study-oid
  ::df/oid-ref)

(s/def ::metadata-version-oid
  ::df/oid-ref)

(s/def :odm/include
  (s/keys :req [::study-oid ::metadata-version-oid]))
