(ns odm.clinical-data-test
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
         [odm.clinical-data]))

(st/instrument)

(deftest clinical-data-test
  (testing "Valid clinical data"
    (are [x] (s/valid? :odm/clinical-data x)
      #:odm.clinical-data
          {:study-oid "S01"
           :metadata-version-oid "MD01"}

      #:odm.clinical-data
          {:study-oid "S01"
           :metadata-version-oid "MD01"
           :subject-data
           [#:odm.subject-data
               {:subject-key "SK01"}]}))

  (testing "Invalid subject data"
    (given-problems :odm/clinical-data
      #:odm.clinical-data
          {:study-oid "S01"
           :metadata-version-oid "MD01"
           :subject-data nil}
      [first :path] := [:odm.clinical-data/subject-data]
      [first :pred] := 'coll?)

    (given-problems :odm/clinical-data
      #:odm.clinical-data
          {:study-oid "S01"
           :metadata-version-oid "MD01"
           :subject-data [{}]}
      [first :path] := [:odm.clinical-data/subject-data]
      [first :pred] := '(contains? % :odm.subject-data/subject-key)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/clinical-data 1)))))
