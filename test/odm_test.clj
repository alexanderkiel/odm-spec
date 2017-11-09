(ns odm-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer :all]
            [odm]
            [juxt.iota :refer [given]]))

(deftest oid-test
  (testing "nil is invalid"
    (is (not (s/valid? :odm/oid nil))))

  (testing "numbers are invalid"
    (is (not (s/valid? :odm/oid 1))))

  (testing "Blank strings are invalid"
    (is (not (s/valid? :odm/oid ""))))

  (testing "Whitespace only strings are invalid"
    (is (not (s/valid? :odm/oid " "))))

  (testing "Conform trims OIDs"
    (are [x y] (= y (s/conform :odm/oid x))
      "foo" "foo"
      "foo " "foo"
      " foo" "foo"
      " foo " "foo"))

  (testing "Generator available"
    (is (s/gen :odm/oid))))
