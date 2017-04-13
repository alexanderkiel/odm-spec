(ns odm.item-ref-test
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
         [odm.item-ref]))

(st/instrument)

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
      [first :pred] := '(contains? % :odm.item-ref/item-oid)
      [second :pred] := '(contains? % :odm/mandatory)))

  (testing "Invalid collection exception condition OID"
    (given-problems :odm/item-ref
      #:odm.item-ref
          {:item-oid "I01"
           :odm/mandatory true
           :odm.ref/collection-exception-condition-oid nil}
      [first :path] := [:odm.ref/collection-exception-condition-oid]
      [first :pred] := 'string?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-ref 1)))))
