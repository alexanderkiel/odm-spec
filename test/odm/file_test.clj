(ns odm.file-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.file]
            [odm-spec.test-util :refer [given-problems]])
  (:import [java.util Date]))

(deftest file-test
  (testing "Valid files"
    (are [x] (s/valid? :odm/file x)
      #:odm.file
          {:oid "FI01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"}

      #:odm.file
          {:oid "FI01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :studies
           [#:odm.study
               {:oid "S01"
                :name "foo"
                :description "bar"
                :protocol-name "baz"}]}

      #:odm.file
          {:oid "FI01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :studies
           [#:odm.study
               {:oid "S01"
                :name "foo"
                :description "bar"
                :protocol-name "baz"
                :metadata-versions
                [#:odm.metadata-version
                    {:oid "V01"
                     :name "foo"
                     :form-defs
                     [#:odm.form-def
                         {:oid "F01"
                          :name "foo"
                          :odm.def/repeating false}]}]}]}

      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :granularity :single-subject
           :clinical-data
           [#:odm.clinical-data
               {:study-oid "S01"
                :metadata-version-oid "MD01"
                :subject-data
                [#:odm.subject-data
                    {:subject-key "SK01"
                     :study-event-data
                     [#:odm.study-event-data
                         {:study-event-oid "SE01"}]}]}]}))

  (testing "Missing keys"
    (given-problems :odm/file
      {}
      [0 :pred] := '(contains? % :odm.file/oid)
      [1 :pred] := '(contains? % :odm.file/type)))

  (testing "Invalid granularity"
    (given-problems :odm/file
      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :granularity :foo}
      [0 :path] := [:odm.file/granularity]
      [0 :pred] := #{:all :metadata :admin-data :reference-data
                     :all-clinical-data :single-site :single-subject}))

  (testing "Invalid creation date-time"
    (given-problems :odm/file
      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time "2017"}
      [0 :path] := [:odm.file/creation-date-time]
      [0 :pred] := 'inst?))

  (testing "Invalid prior file OID"
    (given-problems :odm/file
      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :prior-oid ""}
      [0 :path] := [:odm.file/prior-oid]
      [0 :pred] := '(complement blank?)))

  (testing "Invalid as-of date-time"
    (given-problems :odm/file
      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :as-of-date-time "2017"}
      [0 :path] := [:odm.file/as-of-date-time]
      [0 :pred] := 'inst?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/file 1)))))
