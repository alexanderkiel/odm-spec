(ns odm.form-def-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.form-def]
            [odm-spec.test-util :refer [given-problems]]))

(deftest form-def-test
  (testing "Valid form definitions"
    (are [x] (s/valid? :odm/form-def x)
      #:odm.form-def
          {:oid "F01"
           :name "foo"
           :repeating true}))

  (testing "Duplicate item group ref OIDs"
    (given-problems :odm/form-def
      #:odm.form-def
          {:oid "F01"
           :name "foo"
           :repeating false
           :type :common
           :item-group-refs
           [#:odm.item-group-ref
               {:item-group-oid "IG01"
                :odm/mandatory true}
            #:odm.item-group-ref
                {:item-group-oid "IG01"
                 :odm/mandatory true}]}
      [0 :path] := [:odm.form-def/item-group-refs]
      [0 :pred] := '(partial distinct-oids? :odm.item-group-ref/item-group-oid)))

  (testing "Duplicate order numbers in item group refs"
    (given-problems :odm/form-def
      #:odm.form-def
          {:oid "F01"
           :name "foo"
           :repeating false
           :type :common
           :item-group-refs
           [#:odm.item-group-ref
               {:item-group-oid "IG01"
                :odm/mandatory true
                :odm/order-number 1}
            #:odm.item-group-ref
                {:item-group-oid "IG02"
                 :odm/mandatory true
                 :odm/order-number 1}]}
      [0 :path] := [:odm.form-def/item-group-refs]
      [0 :pred] := '(distinct-order-numbers? %)))

  (testing "Invalid aliases"
    (given-problems :odm/form-def
      #:odm.form-def
          {:oid "F01"
           :name "foo"
           :repeating false
           :type :common
           :odm/aliases 1}
      [0 :path] := [:odm/aliases]
      [0 :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/form-def 1)))))
