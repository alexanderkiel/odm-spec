(ns odm.rfc-5646-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.rfc-5646]
            [odm-spec.test-util :refer [given-problems]]))

(deftest lang-tag-test
  (testing "Valid tags"
    (are [tag cval] (= cval (s/conform :rfc-5646/lang-tag tag))
      ;; Simple language subtag
      "de" {:language "de"}
      "fr" {:language "fr"}
      "ja" {:language "ja"}

      ;; Language subtag plus Script subtag
      "zh-Hant" {:language "zh" :script "Hant"}
      "zh-Hans" {:language "zh" :script "Hans"}

      ;; Language-Script-Region
      "zh-Hans-CN" {:language "zh" :script "Hans" :region "CN"}
      "sr-Latn-RS" {:language "sr" :script "Latn" :region "RS"}

      ;; Language-Region
      "de-DE" {:language "de" :region "DE"}
      "en-US" {:language "en" :region "US"}
      "es-419" {:language "es" :region "419"}))

  (testing "Invalid tags"
    (given-problems :rfc-5646/lang-tag "de-419-DE"
      [0 :path] := [:region :val :un-m49]
      [0 :pred] := 'char-digit?
      [0 :val] := \-)

    (given-problems :rfc-5646/lang-tag "a-DE"
      [0 :path] := [:language]
      [0 :pred] := 'char-alpha?
      [0 :val] := \-))

  (testing "Generator available"
    (is (doall (s/exercise :rfc-5646/lang-tag 1)))))
