(ns odm.measurement-unit-test
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
         [odm.measurement-unit]))

(st/instrument)

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
      [first :pred] := `(fn [~'%] (contains? ~'% :odm.measurement-unit/oid))
      [second :pred] := `(fn [~'%] (contains? ~'% :odm.measurement-unit/name))
      [#(nth % 2) :pred] := `(fn [~'%] (contains? ~'% :odm.measurement-unit/symbol))))

  (testing "Invalid symbol"
    (given-problems :odm/measurement-unit
      #:odm.measurement-unit
          {:oid "U01"
           :name "kilogram"
           :symbol "kg"}
      [first :path] := [:odm.measurement-unit/symbol]
      [first :pred] := `coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/measurement-unit 1)))))
