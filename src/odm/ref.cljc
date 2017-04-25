(ns odm.ref
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.condition-def :as c]))

(s/def ::collection-exception-condition-oid
  ::c/oid)


