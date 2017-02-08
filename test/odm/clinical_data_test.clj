(ns odm.clinical-data-test
  (:require [clojure.pprint :refer [pprint]]
            [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.clinical-data]
            [odm-spec.test-util :refer [given-problems]]))

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
      [0 :path] := [:odm.clinical-data/subject-data]
      [0 :pred] := 'coll?)

    (given-problems :odm/clinical-data
      #:odm.clinical-data
          {:study-oid "S01"
           :metadata-version-oid "MD01"
           :subject-data [{}]}
      [0 :path] := [:odm.clinical-data/subject-data]
      [0 :pred] := '(contains? % :odm.subject-data/subject-key)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/clinical-data 1)))))

(comment
  (pprint (map first (s/exercise :odm/clinical-data 1)))
  )
