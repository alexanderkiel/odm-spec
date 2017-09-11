(ns odm.form-ref
  "3.1.1.3.3.1 - FormRef"
  (:require
    #?(:clj [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])
            [odm.common]
            [odm.condition-def :as c]
            [odm.form-def :as f]
            [odm.ref]))

(s/def ::form-oid
  ::f/oid)

(s/def :odm/form-ref
  (s/keys :req [::form-oid :odm/mandatory]
          :opt [:odm/order-number :odm.ref/collection-exception-condition-oid]))
