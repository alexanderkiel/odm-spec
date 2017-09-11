(ns odm.study-event-data-test
  (:require
    #?@(:clj
        [[clojure.spec.alpha :as s]
         [clojure.spec.test.alpha :as st]
         [clojure.test :refer :all]
         [odm-spec.test-util :refer [given-problems]]]
        :cljs
        [[cljs.spec.alpha :as s]
         [cljs.spec.test.alpha :as st]
         [cljs.test :refer-macros [deftest testing is are]]
         [odm-spec.test-util :refer-macros [given-problems]]])
         [clojure.string :as str]
         [odm.data-formats :as df]
         [odm.study-event-data]
         [odm-spec.util :as u]))

(st/instrument)

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
      [first :path] := [:odm.study-event-data/study-event-repeat-key]
      [first :pred] := `(complement str/blank?)))

  (testing "Invalid transaction type"
    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :odm/tx-type :foo}
      [first :path] := [:odm/tx-type]
      [first :pred] := `df/tx-type?))

  (testing "Invalid form data"
    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :form-data nil}
      [first :path] := [:odm.study-event-data/form-data]
      [first :pred] := `coll?)

    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :form-data [{}]}
      [first :path] := [:odm.study-event-data/form-data]
      [first :pred] := `(fn [~'%] (contains? ~'% :odm.form-data/form-oid))))

  (testing "Duplicate form data OIDs"
    (given-problems :odm/study-event-data
      #:odm.study-event-data
          {:study-event-oid "SE01"
           :form-data
           [#:odm.form-data
               {:form-oid "F01"}
            #:odm.form-data
                {:form-oid "F01"}]}
      [first :path] := [:odm.study-event-data/form-data]
      [first :pred] := `(partial u/distinct-oid-repeat-key-pairs?
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
      [first :path] := [:odm.study-event-data/form-data]
      [first :pred] := `(partial u/distinct-oid-repeat-key-pairs?
                                 :odm.form-data/form-oid
                                 :odm.form-data/form-repeat-key)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/study-event-data 1)))))
