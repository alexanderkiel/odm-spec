(ns odm.study-event-def-test
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
         [odm.study-event-def]))

(st/instrument)

(deftest study-event-def-test
  (testing "Valid study event definitions"
    (are [x] (s/valid? :odm/study-event-def x)
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :odm.def/repeating false
           :type :common}))

  (testing "Invalid category"
    (given-problems :odm/study-event-def
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :odm.def/repeating false
           :type :common
           :category nil}
      [first :path] := [:odm.study-event-def/category]
      [first :pred] := 'string?))

  (testing "Duplicate form ref OIDs"
    (given-problems :odm/study-event-def
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :odm.def/repeating false
           :type :common
           :form-refs
           [#:odm.form-ref
               {:form-oid "F01"
                :odm/mandatory true}
            #:odm.form-ref
                {:form-oid "F01"
                 :odm/mandatory true}]}
      [first :path] := [:odm.study-event-def/form-refs]
      [first :pred] := '(partial distinct-values? :odm.form-ref/form-oid)))

  (testing "Duplicate order numbers in form refs"
    (given-problems :odm/study-event-def
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :odm.def/repeating false
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
      [first :path] := [:odm.study-event-def/form-refs]
      [first :pred] := '(distinct-order-numbers? %)))

  (testing "Invalid aliases"
    (given-problems :odm/study-event-def
      #:odm.study-event-def
          {:oid "SE01"
           :name "foo"
           :odm.def/repeating false
           :type :common
           :odm/aliases 1}
      [first :path] := [:odm/aliases]
      [first :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/study-event-def 1)))))
