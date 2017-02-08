(ns odm.study-event-data-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.study-event-data]
            [odm-spec.test-util :refer [given-problems]]))

(deftest study-event-data-test
  (testing "Valid study event data"
    (are [x] (s/valid? :odm/study-event-data x)
      #:odm.study-event-data
          {:study-event-oid "SE01"}

      #:odm.study-event-data
          {:study-event-oid "SE01"
           :study-event-repeat-key "RK01"
           :form-data
           [#:odm.form-data
               {:form-oid "F01"}]}

      #:odm.study-event-data
          {:study-event-oid "SE01"
           :form-data
           [#:odm.form-data
               {:form-oid "F01"
                :form-repeat-key "RK01"}
            #:odm.form-data
                {:form-oid "F01"
                 :form-repeat-key "RK02"}]}))

  (testing "Invalid study-event repeat-key"
    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :study-event-repeat-key ""}
      [0 :path] := [:odm.study-event-data/study-event-repeat-key]
      [0 :pred] := '(complement blank?)))

  (testing "Invalid transaction type"
    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :odm/tx-type :foo}
      [0 :path] := [:odm/tx-type]
      [0 :pred] := 'tx-type?))

  (testing "Invalid form data"
    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :form-data nil}
      [0 :path] := [:odm.study-event-data/form-data]
      [0 :pred] := 'coll?)

    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :form-data [{}]}
      [0 :path] := [:odm.study-event-data/form-data]
      [0 :pred] := '(contains? % :odm.form-data/form-oid)))

  (testing "Duplicate form data OIDs"
    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :form-data
           [#:odm.form-data
               {:form-oid "F01"}
            #:odm.form-data
               {:form-oid "F01"}]}
      [0 :path] := [:odm.study-event-data/form-data]
      [0 :pred] := '(partial distinct-oid-repeat-key-pairs?
                             :odm.form-data/form-oid
                             :odm.form-data/form-repeat-key))

    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :form-data
           [#:odm.form-data
               {:form-oid "F01"
                :form-repeat-key "RK01"}
            #:odm.form-data
               {:form-oid "F01"
                :form-repeat-key "RK01"}]}
      [0 :path] := [:odm.study-event-data/form-data]
      [0 :pred] := '(partial distinct-oid-repeat-key-pairs?
                             :odm.form-data/form-oid
                             :odm.form-data/form-repeat-key)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/study-event-data 1)))))
