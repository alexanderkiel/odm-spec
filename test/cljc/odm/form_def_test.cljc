(ns odm.form-def-test
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
         [odm.form-def]))

(st/instrument)

(deftest form-def-test
  (testing "Valid form definitions"
    (are [x] (s/valid? :odm/form-def x)
      #:odm.form-def
          {:oid "F01"
           :name "foo"
           :odm.def/repeating true}))

  (testing "Duplicate item group ref OIDs"
    (given-problems :odm/form-def
      #:odm.form-def
          {:oid "F01"
           :name "foo"
           :odm.def/repeating false
           :type :common
           :item-group-refs
           [#:odm.item-group-ref
               {:item-group-oid "IG01"
                :odm/mandatory true}
            #:odm.item-group-ref
                {:item-group-oid "IG01"
                 :odm/mandatory true}]}
      [first :path] := [:odm.form-def/item-group-refs]
      [first :pred] := '(partial distinct-values? :odm.item-group-ref/item-group-oid)))

  (testing "Duplicate order numbers in item group refs"
    (given-problems :odm/form-def
      #:odm.form-def
          {:oid "F01"
           :name "foo"
           :odm.def/repeating false
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
      [first :path] := [:odm.form-def/item-group-refs]
      [first :pred] := '(distinct-order-numbers? %)))

  (testing "Invalid aliases"
    (given-problems :odm/form-def
      #:odm.form-def
          {:oid "F01"
           :name "foo"
           :odm.def/repeating false
           :type :common
           :odm/aliases 1}
      [first :path] := [:odm/aliases]
      [first :pred] := 'coll?))

  #?(:clj
     (testing "Generator available"
       (is (doall (s/exercise :odm/form-def 1))))))
