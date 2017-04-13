(ns odm.alias
  "3.1.1.3.6.6 - Alias

  An alias provides an additional name for an entity. The context specifies the
  application domain in which the additional name is relevant."
  (:require
    #?(:clj [clojure.spec :as s]
       :cljs [cljs.spec :as s])
            [odm.data-formats :as df]))

;; The application domain in which the alias is relevant.
(s/def ::context
  ::df/text)

(s/def ::name
  ::df/text)

(s/def :odm/alias
  (s/keys :req [::context ::name]))

(s/def :odm/aliases
  (s/coll-of :odm/alias :gen-max 3))
