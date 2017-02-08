(ns odm.study-event-def-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.study-event-def]
            [odm-spec.test-util :refer [given-problems]]))

(deftest study-event-def-test
  (testing "Valid study event definitions"
    (are [x] (s/valid? :odm/study-event-def x)
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :repeating false
           :type :common}))

  (testing "Invalid category"
    (given-problems :odm/study-event-def
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :repeating false
           :type :common
           :category nil}
      [0 :path] := [:odm.study-event-def/category]
      [0 :pred] := 'string?))

  (testing "Duplicate form ref OIDs"
    (given-problems :odm/study-event-def
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :repeating false
           :type :common
           :form-refs
           [#:odm.form-ref
               {:form-oid "F01"
                :odm/mandatory true}
            #:odm.form-ref
                {:form-oid "F01"
                 :odm/mandatory true}]}
      [0 :path] := [:odm.study-event-def/form-refs]
      [0 :pred] := '(partial distinct-oids? :odm.form-ref/form-oid)))

  (testing "Duplicate order numbers in form refs"
    (given-problems :odm/study-event-def
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :repeating false
           :type :common
           :form-refs
           [#:odm.form-ref
               {:form-oid "F01"
                :odm/mandatory true
                :odm/order-number 1}
            #:odm.form-ref
                {:form-oid "F02"
                 :odm/mandatory true
                 :odm/order-number 1}]}
      [0 :path] := [:odm.study-event-def/form-refs]
      [0 :pred] := '(distinct-order-numbers? %)))

  (testing "Invalid aliases"
    (given-problems :odm/study-event-def
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :repeating false
           :type :common
           :odm/aliases 1}
      [0 :path] := [:odm/aliases]
      [0 :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/study-event-def 1)))))
