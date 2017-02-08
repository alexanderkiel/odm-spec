(ns odm.study-event-ref-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.study-event-ref]
            [odm-spec.test-util :refer [given-problems]]))

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
      [0 :pred] := '(contains? % :odm.study-event-ref/study-event-oid)
      [1 :pred] := '(contains? % :odm/mandatory)))

  (testing "Invalid collection exception condition OID"
    (given-problems :odm/study-event-ref
      #:odm.study-event-ref
          {:study-event-oid "SE01"
           :odm/mandatory true
           :odm.ref/collection-exception-condition-oid nil}
      [0 :path] := [:odm.ref/collection-exception-condition-oid]
      [0 :pred] := 'string?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/study-event-ref 1)))))
