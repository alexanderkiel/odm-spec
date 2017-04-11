(ns odm.rfc-5646
  (:require [clojure.spec :as s]))

(s/def ::to-str
  (s/conformer #(apply str %)))

(s/def ::extract-val
  (s/conformer #(:val %)))

(defn- char-alpha? [x]
  (and (char? x) (Character/isLetter ^char x)))

(defn- char-digit? [x]
  (and (char? x) (Character/isDigit ^char x)))

(s/def ::language
  (s/& (s/+ char-alpha?) #(<= 2 (count %) 3) ::to-str))

(s/def ::script
  (s/& (s/+ char-alpha?) #(= 4 (count %)) ::to-str))

(s/def ::region
  (s/& (s/alt :iso-3166-1 (s/& (s/+ char-alpha?) #(= 2 (count %)) ::to-str)
              :un-m49 (s/& (s/+ char-digit?) #(= 3 (count %)) ::to-str))
       (s/conformer second)))

;; RFC 5646 language tag
(s/def :rfc-5646/lang-tag
  (s/with-gen
    (s/and
      string?
      (s/conformer seq)
      (s/cat :language ::language
             :script (s/? (s/& (s/cat :_ #{\-} :val ::script) ::extract-val))
             :region (s/? (s/& (s/cat :_ #{\-} :val ::region) ::extract-val))))
    #(s/gen #{"de"})))
