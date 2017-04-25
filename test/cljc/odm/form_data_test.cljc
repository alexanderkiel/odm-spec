(ns odm.form-data-test
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
         [odm.form-data]))

(st/instrument)

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
      [first :pred] := '(contains? % :odm.form-data/form-oid)))

  (testing "Invalid form repeat-key"
    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :form-repeat-key ""}
      [first :path] := [:odm.form-data/form-repeat-key]
      [first :pred] := '(complement blank?)))

  (testing "Invalid transaction type"
    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :odm/tx-type :foo}
      [first :path] := [:odm/tx-type]
      [first :pred] := 'tx-type?))

  (testing "Invalid item-group data"
    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :item-group-data nil}
      [first :path] := [:odm.form-data/item-group-data]
      [first :pred] := 'coll?)

    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :item-group-data [{}]}
      [first :path] := [:odm.form-data/item-group-data]
      [first :pred] := '(contains? % :odm.item-group-data/item-group-oid)))

  (testing "Duplicate item-group data OIDs"
    (given-problems :odm/form-data
      #:odm.form-data
          {:form-oid "F01"
           :item-group-data
           [#:odm.item-group-data
               {:item-group-oid "IG01"}
            #:odm.item-group-data
                {:item-group-oid "IG01"}]}
      [first :path] := [:odm.form-data/item-group-data]
      [first :pred] := '(partial distinct-oid-repeat-key-pairs?
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
      [first :path] := [:odm.form-data/item-group-data]
      [first :pred] := '(partial distinct-oid-repeat-key-pairs?
                                 :odm.item-group-data/item-group-oid
                                 :odm.item-group-data/item-group-repeat-key)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/form-data 1)))))
