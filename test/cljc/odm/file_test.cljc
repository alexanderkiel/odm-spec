(ns odm.file-test
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
         [odm.file]))

(st/instrument)

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
      [first :pred] := '(contains? % :odm.file/oid)
      [second :pred] := '(contains? % :odm.file/type)))

  (testing "Invalid granularity"
    (given-problems :odm/file
      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :granularity :foo}
      [first :path] := [:odm.file/granularity]
      [first :pred] := #{:all :metadata :admin-data :reference-data
                     :all-clinical-data :single-site :single-subject}))

  (testing "Invalid creation date-time"
    (given-problems :odm/file
      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time "2017"}
      [first :path] := [:odm.file/creation-date-time]
      [first :pred] := 'inst?))

  (testing "Invalid prior file OID"
    (given-problems :odm/file
      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :prior-oid ""}
      [first :path] := [:odm.file/prior-oid]
      [first :pred] := '(complement blank?)))

  (testing "Invalid as-of date-time"
    (given-problems :odm/file
      #:odm.file
          {:oid "F01"
           :type :snapshot
           :creation-date-time #inst "2017-02-13T07:50:00.000Z"
           :as-of-date-time "2017"}
      [first :path] := [:odm.file/as-of-date-time]
      [first :pred] := 'inst?))

  #?(:clj
     (testing "Generator available"
       (is (doall (s/exercise :odm/file 1))))))
