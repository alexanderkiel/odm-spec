(ns odm.study-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.study]
            [odm-spec.test-util :refer [given-problems]]))

(deftest study-test
  (testing "Valid studies"
    (are [x] (s/valid? :odm/study x)
      #:odm.study
          {:oid "S01"
           :name "foo"
           :description "bar"
           :protocol-name "baz"}

      #:odm.study
          {:oid "S01"
           :name "foo"
           :description "bar"
           :protocol-name "baz"
           :measurement-units
           [#:odm.measurement-unit
               {:oid "U01"
                :name "kilogram"
                :symbol {:default "kg"}}]}

      #:odm.study
          {:oid "S01"
           :name "foo"
           :description "bar"
           :protocol-name "baz"
           :metadata-versions
           [#:odm.metadata-version
               {:oid "V01"
                :name "foo"}]}))

  (testing "Missing keys"
    (given-problems :odm/study
      {}
      [0 :pred] := '(contains? % :odm.study/oid)
      [1 :pred] := '(contains? % :odm.study/name)
      [2 :pred] := '(contains? % :odm.study/description)
      [3 :pred] := '(contains? % :odm.study/protocol-name)))

  (testing "Duplicate measurement unit OIDs"
    (given-problems :odm/study
      #:odm.study
          {:oid "S01"
           :name "foo"
           :description "bar"
           :protocol-name "baz"
           :measurement-units
           [#:odm.measurement-unit
               {:oid "U01"
                :name "kilogram"
                :symbol {:default "kg"}}
            #:odm.measurement-unit
               {:oid "U01"
                :name "kilogram"
                :symbol {:default "kg"}}]}
      [0 :path] := [:odm.study/measurement-units]
      [0 :pred] := '(partial distinct-oids? :odm.measurement-unit/oid)))

  (testing "Duplicate metadata version OIDs"
    (given-problems :odm/study
      #:odm.study
          {:oid "S01"
           :name "foo"
           :description "bar"
           :protocol-name "baz"
           :metadata-versions
           [#:odm.metadata-version
               {:oid "V01"
                :name "foo"}
            #:odm.metadata-version
               {:oid "V01"
                :name "foo"}]}
      [0 :path] := [:odm.study/metadata-versions]
      [0 :pred] := '(partial distinct-oids? :odm.metadata-version/oid)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/study 1)))))
