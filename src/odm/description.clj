(ns odm.description
  "3.1.1.3.2.1 - Description"
  (:require [clojure.spec :as s]
            [odm.data-formats :as df]))

(s/def :odm/description
  ::df/translated-text)
