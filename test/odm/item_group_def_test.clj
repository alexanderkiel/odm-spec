(ns odm.item-group-def-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.item-group-def]
            [odm-spec.test-util :refer [given-problems]]))

(deftest item-group-def-test
  (testing "Valid item group definitions"
    (are [x] (s/valid? :odm/item-group-def x)
      #:odm.item-group-def
          {:oid "IG01"
           :name "foo"
           :odm.def/repeating true}))

  (testing "Duplicate item group ref OIDs"
    (given-problems :odm/item-group-def
      #:odm.item-group-def
          {:oid "IG01"
           :name "foo"
           :odm.def/repeating false
           :item-refs
           [#:odm.item-ref
               {:item-oid "I01"
                :odm/mandatory true}
            #:odm.item-ref
                {:item-oid "I01"
                 :odm/mandatory true}]}
      [0 :path] := [:odm.item-group-def/item-refs]
      [0 :pred] := '(partial distinct-oids? :odm.item-ref/item-oid)))

  (testing "Duplicate order numbers in item group refs"
    (given-problems :odm/item-group-def
      #:odm.item-group-def
          {:oid "IG01"
           :name "foo"
           :odm.def/repeating false
           :item-refs
           [#:odm.item-ref
               {:item-oid "I01"
                :odm/mandatory true
                :odm/order-number 1}
            #:odm.item-ref
                {:item-oid "I02"
                 :odm/mandatory true
                 :odm/order-number 1}]}
      [0 :path] := [:odm.item-group-def/item-refs]
      [0 :pred] := '(distinct-order-numbers? %)))

  (testing "Invalid aliases"
    (given-problems :odm/item-group-def
      #:odm.item-group-def
          {:oid "IG01"
           :name "foo"
           :odm.def/repeating false
           :type :common
           :odm/aliases 1}
      [0 :path] := [:odm/aliases]
      [0 :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-group-def 1)))))
