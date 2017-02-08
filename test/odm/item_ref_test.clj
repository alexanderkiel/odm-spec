(ns odm.item-ref-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.item-ref]
            [odm-spec.test-util :refer [given-problems]]))

(deftest item-ref-test
  (testing "Valid item references"
    (are [x] (s/valid? :odm/item-ref x)
      #:odm.item-ref
          {:item-oid "I01"
           :odm/mandatory true}

      #:odm.item-ref
          {:item-oid "I01"
           :odm/order-number 1
           :odm/mandatory true}))

  (testing "Missing keys"
    (given-problems :odm/item-ref
      {}
      [0 :pred] := '(contains? % :odm.item-ref/item-oid)
      [1 :pred] := '(contains? % :odm/mandatory)))

  (testing "Invalid collection exception condition OID"
    (given-problems :odm/item-ref
      #:odm.item-ref
          {:item-oid "I01"
           :odm/mandatory true
           :odm.ref/collection-exception-condition-oid nil}
      [0 :path] := [:odm.ref/collection-exception-condition-oid]
      [0 :pred] := 'string?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-ref 1)))))
