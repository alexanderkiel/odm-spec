(ns odm-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm]
            [juxt.iota :refer [given]]))

(defmacro given-problems [spec val & body]
  `(given (::s/problems (s/explain-data ~spec ~val))
     ~@body))

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

(deftest file-type-test
  (are [x] (s/valid? :odm/file-type x)
    :snapshot
    :transactional)
  (given-problems :odm/file-type nil
    [0 :pred] := #{:snapshot :transactional}
    [0 :via] := [:odm/file-type])
  (given-problems :odm/file-type :foo
    [0 :pred] := #{:snapshot :transactional}
    [0 :via] := [:odm/file-type])
  (testing "generator is available"
    (is (s/exercise :odm/file-type))))
