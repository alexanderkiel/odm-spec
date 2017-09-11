(ns odm.item-group-def-test
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
         [odm.item-group-def]
         [odm-spec.util :as u]))

(st/instrument)

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
      [first :path] := [:odm.item-group-def/item-refs]
      [first :pred] := `(partial u/distinct-values? :odm.item-ref/item-oid)))

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
      [first :path] := [:odm.item-group-def/item-refs]
      [first :pred] := `(fn [~'%] (u/distinct-order-numbers? ~'%))))

  (testing "Invalid aliases"
    (given-problems :odm/item-group-def
      #:odm.item-group-def
          {:oid "IG01"
           :name "foo"
           :odm.def/repeating false
           :type :common
           :odm/aliases 1}
      [first :path] := [:odm/aliases]
      [first :pred] := `coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-group-def 1)))))
