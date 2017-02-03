(ns odm.util
  (:require [clojure.spec :as s]
            [clojure.string :as str]))

(s/def ::non-blank-string? (s/and string? (complement str/blank?)))

(s/def ::oid
  (s/with-gen
    (s/conformer #(s/conform ::non-blank-string? (when (string? %) (str/trim %))))
    #(s/gen ::non-blank-string?)))
