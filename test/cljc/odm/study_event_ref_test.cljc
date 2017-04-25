(ns odm.study-event-ref-test
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
         [odm.study-event-ref]))

(st/instrument)

(deftest study-event-ref-test
  (testing "Valid study event references"
    (are [x] (s/valid? :odm/study-event-ref x)
      #:odm.study-event-ref
          {:study-event-oid "SE01"
           :odm/mandatory true}

      #:odm.study-event-ref
          {:study-event-oid "SE01"
           :odm/order-number 1
           :odm/mandatory true}))

  (testing "Missing keys"
    (given-problems :odm/study-event-ref
      {}
      [first :pred] := '(contains? % :odm.study-event-ref/study-event-oid)
      [second :pred] := '(contains? % :odm/mandatory)))

  (testing "Invalid collection exception condition OID"
    (given-problems :odm/study-event-ref
      #:odm.study-event-ref
          {:study-event-oid "SE01"
           :odm/mandatory true
           :odm.ref/collection-exception-condition-oid nil}
      [first :path] := [:odm.ref/collection-exception-condition-oid]
      [first :pred] := 'string?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/study-event-ref 1)))))
