(ns odm.item-group-data-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.item-group-data]
            [odm-spec.test-util :refer [given-problems]]))

(deftest item-group-data-test
  (testing "Valid item-group data"
    (are [x] (s/valid? :odm/item-group-data x)
      #:odm.item-group-data
          {:item-group-oid "IG01"}

      #:odm.item-group-data
          {:item-group-oid "IG01"
           :item-data
           [#:odm.item-data
               {:item-oid "I01"
                :data-type :string
                :string-value "foo"}]}))

  (testing "Missing item-group OID"
    (given-problems :odm/item-group-data
      {}
      [0 :pred] := '(contains? % :odm.item-group-data/item-group-oid)))

  (testing "Invalid item-group repeat key"
    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :item-group-repeat-key ""}
      [0 :path] := [:odm.item-group-data/item-group-repeat-key]
      [0 :pred] := '(complement blank?)))

  (testing "Invalid transaction type"
    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :odm/tx-type :foo}
      [0 :path] := [:odm/tx-type]
      [0 :pred] := 'tx-type?))

  (testing "Invalid item data"
    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :item-data nil}
      [0 :path] := [:odm.item-group-data/item-data]
      [0 :pred] := 'coll?)

    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :item-data [{}]}
      [0 :path] := [:odm.item-group-data/item-data nil]
      [0 :pred] := 'item-data-spec))

  (testing "Duplicate item data OIDs"
    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :item-data
           [#:odm.item-data
               {:item-oid "I01"
                :data-type :string
                :string-value "foo"}
            #:odm.item-data
                {:item-oid "I01"
                 :data-type :string
                 :string-value "foo"}]}
      [0 :path] := [:odm.item-group-data/item-data]
      [0 :pred] := '(partial distinct-oids? :odm.item-data/item-oid)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-group-data 1)))))
