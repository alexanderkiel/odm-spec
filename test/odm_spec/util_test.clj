(ns odm-spec.util-test
  (:require [clojure.test :refer :all]
            [odm-spec.util :refer :all]))

(deftest distinct-oids?-test
  (testing "One OID is good"
    (is (distinct-oids? :oid [{:oid "1"}])))

  (testing "Distinct OID's are good"
    (is (distinct-oids? :oid [{:oid "1"} {:oid "2"}])))

  (testing "Duplicate OID's are bad"
    (is (not (distinct-oids? :oid [{:oid "1"} {:oid "1"}])))))

(deftest distinct-order-numbers?-test
  (testing "No order numbers are good"
    (is (distinct-order-numbers? [{} {}])))

  (testing "Distinct order numbers are good"
    (is (distinct-order-numbers?
          [{:odm/order-number 1}
           {:odm/order-number 2}]))))
