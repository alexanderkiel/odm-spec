(ns odm.item-group-data-test
  (:require
    #?@(:clj
        [[clojure.spec.alpha :as s]
         [clojure.spec.test.alpha :as st]
         [clojure.test :refer :all]
         [odm-spec.test-util :refer [given-problems]]]
        :cljs
        [[cljs.spec.alpha :as s]
         [cljs.spec.test.alpha :as st]
         [cljs.test :refer-macros [deftest testing is are]]
         [odm-spec.test-util :refer-macros [given-problems]]])
         [clojure.string :as str]
         [odm.data-formats :as df]
         [odm.item-data :as item-data]
         [odm.item-group-data]
         [odm-spec.util :as u]))

(st/instrument)

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
      [first :pred] := `(fn [~'%] (contains? ~'% :odm.item-group-data/item-group-oid))))

  (testing "Invalid item-group repeat key"
    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :item-group-repeat-key ""}
      [first :path] := [:odm.item-group-data/item-group-repeat-key]
      [first :pred] := `(complement str/blank?)))

  (testing "Invalid transaction type"
    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :odm/tx-type :foo}
      [first :path] := [:odm/tx-type]
      [first :pred] := `df/tx-type?))

  (testing "Invalid item data"
    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :item-data nil}
      [first :path] := [:odm.item-group-data/item-data]
      [first :pred] := `coll?)

    (given-problems :odm/item-group-data
      #:odm.item-group-data
          {:item-group-oid "IG01"
           :item-data [{}]}
      [first :path] := [:odm.item-group-data/item-data nil]
      [first :pred] := `item-data/item-data-spec))

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
      [first :path] := [:odm.item-group-data/item-data]
      [first :pred] := `(partial u/distinct-values? :odm.item-data/item-oid)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-group-data 1)))))
