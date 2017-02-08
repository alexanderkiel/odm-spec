(ns odm.form-data-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.form-data]
            [odm-spec.test-util :refer [given-problems]]))

(deftest form-data-test
  (testing "Valid form data"
    (are [x] (s/valid? :odm/form-data x)
      #:odm.form-data
          {:form-oid "F01"}

      #:odm.form-data
          {:form-oid "F01"
           :item-group-data
           [#:odm.item-group-data
               {:item-group-oid "IG01"}]}

      #:odm.form-data
          {:form-oid "F01"
           :item-group-data
           [#:odm.item-group-data
               {:item-group-oid "IG01"
                :item-group-repeat-key "RK01"}
            #:odm.item-group-data
                {:item-group-oid "IG01"
                 :item-group-repeat-key "RK02"}]}))

  (testing "Missing form OID"
    (given-problems :odm/form-data
      {}
      [0 :pred] := '(contains? % :odm.form-data/form-oid)))

  (testing "Invalid form repeat-key"
    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :form-repeat-key ""}
      [0 :path] := [:odm.form-data/form-repeat-key]
      [0 :pred] := '(complement blank?)))

  (testing "Invalid transaction type"
    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :odm/tx-type :foo}
      [0 :path] := [:odm/tx-type]
      [0 :pred] := 'tx-type?))

  (testing "Invalid item-group data"
    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :item-group-data nil}
      [0 :path] := [:odm.form-data/item-group-data]
      [0 :pred] := 'coll?)

    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :item-group-data [{}]}
      [0 :path] := [:odm.form-data/item-group-data]
      [0 :pred] := '(contains? % :odm.item-group-data/item-group-oid)))

  (testing "Duplicate item-group data OIDs"
    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :item-group-data
           [#:odm.item-group-data
               {:item-group-oid "IG01"}
            #:odm.item-group-data
               {:item-group-oid "IG01"}]}
      [0 :path] := [:odm.form-data/item-group-data]
      [0 :pred] := '(partial distinct-oid-repeat-key-pairs?
                             :odm.item-group-data/item-group-oid
                             :odm.item-group-data/item-group-repeat-key))

    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :item-group-data
           [#:odm.item-group-data
               {:item-group-oid "IG01"
                :item-group-repeat-key "RK01"}
            #:odm.item-group-data
               {:item-group-oid "IG01"
                :item-group-repeat-key "RK01"}]}
      [0 :path] := [:odm.form-data/item-group-data]
      [0 :pred] := '(partial distinct-oid-repeat-key-pairs?
                             :odm.item-group-data/item-group-oid
                             :odm.item-group-data/item-group-repeat-key)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/form-data 1)))))
