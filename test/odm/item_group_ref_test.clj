(ns odm.item-group-ref-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.item-group-ref]
            [odm-spec.test-util :refer [given-problems]]))

(deftest item-group-ref-test
  (testing "Valid item group references"
    (are [x] (s/valid? :odm/item-group-ref x)
      #:odm.item-group-ref
          {:item-group-oid "IG01"
           :odm/mandatory true}

      #:odm.item-group-ref
          {:item-group-oid "IG01"
           :odm/order-number 1
           :odm/mandatory true}))

  (testing "Missing keys"
    (given-problems :odm/item-group-ref
      {}
      [0 :pred] := '(contains? % :odm.item-group-ref/item-group-oid)
      [1 :pred] := '(contains? % :odm/mandatory)))

  (testing "Invalid collection exception condition OID"
    (given-problems :odm/item-group-ref
      #:odm.item-group-ref
          {:item-group-oid "IG01"
           :odm/mandatory true
           :odm.ref/collection-exception-condition-oid nil}
      [0 :path] := [:odm.ref/collection-exception-condition-oid]
      [0 :pred] := 'string?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-group-ref 1)))))
