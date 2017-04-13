(ns odm.item-data-test
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
         [odm.item-data]))

(st/instrument)

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
      [first :pred] := '(contains? % :odm.item-data/item-oid)))

  (testing "Invalid data type"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :foo}
      [first :path] := [:foo]
      [first :pred] := 'item-data-spec))

  (testing "Invalid string value"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :string
           :string-value 1}
      [first :path] := [:string :odm.item-data/string-value]
      [first :pred] := 'string?))

  (testing "Invalid integer value"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :integer
           :integer-value 1.1}
      [first :path] := [:integer :odm.item-data/integer-value]
      [first :pred] := 'integer?))

  (testing "Invalid float value"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :float
           :float-value "1"}
      [first :path] := [:float :odm.item-data/float-value]))

  (testing "Invalid transaction type"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :string
           :string-value "foo"
           :odm/tx-type :foo}
      [first :path] := [:string :odm/tx-type]
      [first :pred] := 'tx-type?))

  (testing "Invalid measurement unit OID"
    (given-problems :odm/item-data
      #:odm.item-data
          {:item-oid "I01"
           :data-type :integer
           :integer-value 1
           :measurement-unit-oid ""}
      [first :path] := [:integer :odm.item-data/measurement-unit-oid]
      [first :pred] := '(complement blank?)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-data 1)))))
