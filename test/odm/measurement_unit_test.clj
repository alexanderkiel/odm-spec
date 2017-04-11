(ns odm.measurement-unit-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.measurement-unit]
            [odm-spec.test-util :refer [given-problems]]))

(deftest measurement-unit-test
  (testing "Valid measurement units"
    (are [x] (s/valid? :odm/measurement-unit x)
      #:odm.measurement-unit
          {:oid "U01"
           :name "kilogram"
           :symbol [{:lang-tag "de" :text "kg"}]}

      #:odm.measurement-unit
          {:oid "U02"
           :name "meters per second"
           :symbol [{:lang-tag "de" :text "m/s"}]
           :odm/aliases
           [#:odm.alias{:context "latex"
                        :name "$\\frac{m}{s}$"}]}))

  (testing "Missing keys"
    (given-problems :odm/measurement-unit
      {}
      [0 :pred] := '(contains? % :odm.measurement-unit/oid)
      [1 :pred] := '(contains? % :odm.measurement-unit/name)
      [2 :pred] := '(contains? % :odm.measurement-unit/symbol)))

  (testing "Invalid symbol"
    (given-problems :odm/measurement-unit
      #:odm.measurement-unit
          {:oid "U01"
           :name "kilogram"
           :symbol "kg"}
      [0 :path] := [:odm.measurement-unit/symbol]
      [0 :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/measurement-unit 1)))))
