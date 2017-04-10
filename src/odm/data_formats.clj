(ns odm.data-formats
  "2.13 - Data Formats"
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as gen]
            [clojure.string :as str])
  (:import [java.math MathContext]))

;; unbounded integer, same as xs:integer
(s/def ::integer
  integer?)

;; unbounded positive integer, same as xs:positiveInteger
(s/def ::positive-integer
  (s/and integer? pos?))

;; unbounded non-negative integer, same as xs:nonNegativeInteger
(s/def ::non-negative-integer
  (s/and integer? (complement neg?)))

(defn- number-gen
  "Like (s/gen number?) but doesn't generate non-finite doubles because they
  haven't a representation in ODM floats."
  []
  (gen/one-of
    [(gen/large-integer)
     (gen/double* {:infinite? false :NaN? false})]))

;; decimal number with arbitraty precision, same as xs:decimal
(s/def ::float
  (s/with-gen number? number-gen))

;; a instant in time, same as xs:dateTime
(s/def ::date-time
  inst?)

(s/def ::text
  string?)

(s/def ::oid
  (s/and string? (complement str/blank?)))

(s/def ::oid-ref
  (s/and string? (complement str/blank?)))

(s/def ::subject-key
  (let [subject-key (partial format (str "SK%05d"))]
    (s/with-gen (s/and string? (complement str/blank?))
                #(gen/fmap subject-key (gen/choose 1 99999)))))

(s/def ::repeat-key
  (let [repeat-key (partial format (str "RK%02d"))]
    (s/with-gen (s/and string? (complement str/blank?))
                #(gen/fmap repeat-key (gen/choose 1 99)))))

(s/def ::name
  (s/and string? (complement str/blank?)))

(s/def ::sas-name
  (s/and string? #(re-matches #"[\p{Alpha}_][\p{Alpha}\d_]{0,7}" %)))

;; RFC 3066 language tag
(s/def ::lang
  (s/or :tag string? :default #{:default}))

(s/def ::string
  string?)

;; 3.1.1.2.1.1.1 TranslatedText
(s/def ::translated-text
  (s/coll-of (s/keys :req-un [::lang ::text])))

(def tx-type?
  #{:context :insert :remove :update :upsert})
