(ns odm.rfc-5646-test
  (:require
    #?@(:clj
        [[clojure.spec :as s]
         [clojure.spec.test :as st]
         [clojure.test :refer :all]
         [odm-spec.test-util :refer [given-problems]]]
        :cljs
        [[cljs.spec :as s]
         [cljs.spec.test :as st]
         [cljs.test :refer-macros [deftest testing is are]]
         [odm-spec.test-util :refer-macros [given-problems]]])
         [odm.rfc-5646]))

(st/instrument)

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
      [first :path] := [:region :val :un-m49]
      [first :pred] := 'char-digit?
      [first :val] := \-)

    (given-problems :rfc-5646/lang-tag "a-DE"
      [first :path] := [:language]
      [first :pred] := 'char-alpha?
      [first :val] := \-))

  (testing "Generator available"
    (is (doall (s/exercise :rfc-5646/lang-tag 1)))))
