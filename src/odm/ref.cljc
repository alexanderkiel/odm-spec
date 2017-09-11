(ns odm.ref
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.condition-def :as c]))

(s/def ::collection-exception-condition-oid
  ::c/oid)


