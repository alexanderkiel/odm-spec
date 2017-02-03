(ns odm.util
  (:require [clojure.spec :as s]
            [clojure.string :as str]))

(s/def ::non-blank-string? (s/and string? (complement str/blank?)))

(s/def ::oid
  (s/with-gen
    (s/conformer #(s/conform ::non-blank-string? (when (string? %) (str/trim %))))
    #(s/gen ::non-blank-string?)))

(def data-types
  #{:text :integer :float :date :time :date-time :string :boolean :double
    :hex-binary :base64-binary :hex-float :base64-float :partial-date
    :partial-time :partial-date-time :duration-date-time :interval-datetime
    :incomplete-date-time :incomplete-date :incomplete-time :uri})
