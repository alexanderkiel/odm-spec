(ns odm.subject-data-test
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
         [odm.subject-data]))

(st/instrument)

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
      [first :path] := [:odm/tx-type]
      [first :pred] := 'tx-type?))

  (testing "Invalid study-event data"
    (given-problems :odm/subject-data
      #:odm.subject-data
          {:subject-key "SK01"
           :study-event-data nil}
      [first :path] := [:odm.subject-data/study-event-data]
      [first :pred] := 'coll?)

    (given-problems :odm/subject-data
      #:odm.subject-data
          {:subject-key "SK01"
           :study-event-data [{}]}
      [first :path] := [:odm.subject-data/study-event-data]
      [first :pred] := '(contains? % :odm.study-event-data/study-event-oid)))

  (testing "Duplicate study-event data OIDs"
    (given-problems :odm/subject-data
      #:odm.subject-data
          {:subject-key "SK01"
           :study-event-data
           [#:odm.study-event-data
               {:study-event-oid "SE01"}
            #:odm.study-event-data
               {:study-event-oid "SE01"}]}
      [first :path] := [:odm.subject-data/study-event-data]
      [first :pred] := '(partial distinct-oid-repeat-key-pairs?
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
      [first :path] := [:odm.subject-data/study-event-data]
      [first :pred] := '(partial distinct-oid-repeat-key-pairs?
                             :odm.study-event-data/study-event-oid
                             :odm.study-event-data/study-event-repeat-key)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/subject-data 1)))))
