(ns odm.item-data-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.item-data]
            [odm-spec.test-util :refer [given-problems]]))

(deftest item-data-test
  (testing "Valid item data"
    (are [x] (s/valid? :odm/item-data x)
      #:odm.item-data
          {:item-oid "I01"
           :data-type :string
           :string-value "foo"}

      #:odm.item-data
          {:item-oid "I01"
           :data-type :string
           :string-value "foo"
           :tx-type :insert}))

  (testing "Missing item OID"
    (given-problems :odm/item-data
      #:odm.item-data
      {:data-type :string
       :string-value "foo"}
      [0 :pred] := '(contains? % :odm.item-data/item-oid)))

  (testing "Invalid data type"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :foo}
      [0 :path] := [:foo]
      [0 :pred] := 'item-data-spec))

  (testing "Invalid string value"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :string
           :string-value 1}
      [0 :path] := [:string :odm.item-data/string-value]
      [0 :pred] := 'string?))

  (testing "Invalid integer value"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :integer
           :integer-value 1.1}
      [0 :path] := [:integer :odm.item-data/integer-value]
      [0 :pred] := 'integer?))

  (testing "Invalid float value"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :float
           :float-value "1"}
      [0 :path] := [:float :odm.item-data/float-value]))

  (testing "Invalid transaction type"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :string
           :string-value "foo"
           :odm/tx-type :foo}
      [0 :path] := [:string :odm/tx-type]
      [0 :pred] := 'tx-type?))

  (testing "Invalid measurement unit OID"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :integer
           :integer-value 1
           :measurement-unit-oid ""}
      [0 :path] := [:integer :odm.item-data/measurement-unit-oid]
      [0 :pred] := '(complement blank?)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-data 1)))))
