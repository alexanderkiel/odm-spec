(ns odm.data-formats-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.data-formats :as df]))

(deftest sas-name-test
  (testing "Valid SAS names"
    (are [x] (s/valid? ::df/sas-name x)
      "aaaaaaaa"
      "a1234567"
      "_1234567"
      "STUDYID"
      "SUBJID"
      "SEX"))

  (testing "Invalid SAS names"
    (are [x] (not (s/valid? ::df/sas-name x))
      "1"
      "a12345678"
      ""))

  (testing "Generator available"
    (is (doall (s/exercise ::df/sas-name 1)))))

(deftest translated-text-test
  (are [x] (s/valid? ::df/translated-text x)
    [{:lang :default :text "foo"}]

    [{:lang "de" :text "foo"}]

    [{:lang "de" :text "foo"}
     {:lang :default :text "bar"}]

    [{:lang "de" :text "foo"}
     {:lang "en" :text "bar"}])

  (are [x] (not (s/valid? ::df/translated-text x))
    [{}]))
