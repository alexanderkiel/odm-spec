(ns odm.item-group-ref-test
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
         [odm.item-group-ref]))

(st/instrument)

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
      [first :pred] := '(contains? % :odm.item-group-ref/item-group-oid)
      [second :pred] := '(contains? % :odm/mandatory)))

  (testing "Invalid collection exception condition OID"
    (given-problems :odm/item-group-ref
      #:odm.item-group-ref
          {:item-group-oid "IG01"
           :odm/mandatory true
           :odm.ref/collection-exception-condition-oid nil}
      [first :path] := [:odm.ref/collection-exception-condition-oid]
      [first :pred] := 'string?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-group-ref 1)))))
