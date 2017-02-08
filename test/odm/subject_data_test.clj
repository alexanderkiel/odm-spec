(ns odm.subject-data-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.subject-data]
            [odm-spec.test-util :refer [given-problems]]))

(deftest subject-data-test
  (are [x] (s/valid? :odm/subject-data x)
    #:odm.subject-data
        {:subject-key "SK01"}

    #:odm.subject-data
        {:subject-key "SK01"
         :study-event-data
         [#:odm.study-event-data
             {:study-event-oid "SE01"}]}

    #:odm.subject-data
        {:subject-key "SK01"
         :study-event-data
         [#:odm.study-event-data
             {:study-event-oid "SE01"}
          #:odm.study-event-data
              {:study-event-oid "SE02"}]}

    #:odm.subject-data
        {:subject-key "SK01"
         :study-event-data
         [#:odm.study-event-data
             {:study-event-oid "SE01"
              :study-event-repeat-key "RK01"}
          #:odm.study-event-data
              {:study-event-oid "SE01"
               :study-event-repeat-key "RK02"}]})

  (testing "Invalid transaction type"
    (given-problems :odm/subject-data
      #:odm.subject-data
          {:subject-key "SK01"
           :odm/tx-type :foo}
      [0 :path] := [:odm/tx-type]
      [0 :pred] := 'tx-type?))

  (testing "Invalid study-event data"
    (given-problems :odm/subject-data
      #:odm.subject-data
          {:subject-key "SK01"
           :study-event-data nil}
      [0 :path] := [:odm.subject-data/study-event-data]
      [0 :pred] := 'coll?)

    (given-problems :odm/subject-data
      #:odm.subject-data
          {:subject-key "SK01"
           :study-event-data [{}]}
      [0 :path] := [:odm.subject-data/study-event-data]
      [0 :pred] := '(contains? % :odm.study-event-data/study-event-oid)))

  (testing "Duplicate study-event data OIDs"
    (given-problems :odm/subject-data
      #:odm.subject-data
          {:subject-key "SK01"
           :study-event-data
           [#:odm.study-event-data
               {:study-event-oid "SE01"}
            #:odm.study-event-data
               {:study-event-oid "SE01"}]}
      [0 :path] := [:odm.subject-data/study-event-data]
      [0 :pred] := '(partial distinct-oid-repeat-key-pairs?
                             :odm.study-event-data/study-event-oid
                             :odm.study-event-data/study-event-repeat-key))

    (given-problems :odm/subject-data
      #:odm.subject-data
          {:subject-key "SK01"
           :study-event-data
           [#:odm.study-event-data
               {:study-event-oid "SE01"
                :study-event-repeat-key "RK01"}
            #:odm.study-event-data
               {:study-event-oid "SE01"
                :study-event-repeat-key "RK01"}]}
      [0 :path] := [:odm.subject-data/study-event-data]
      [0 :pred] := '(partial distinct-oid-repeat-key-pairs?
                             :odm.study-event-data/study-event-oid
                             :odm.study-event-data/study-event-repeat-key)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/subject-data 1)))))
