(ns odm.condition-def-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.condition-def]))

(deftest condition-def-test
  (testing "Valid condition definitions"
    (are [x] (s/valid? :odm/condition-def x)
      #:odm.condition-def
          {:oid "C01"
           :name "foo"
           :odm/description
           [{:lang :default :text "bar"}]}))

  (testing "Generator available"
    (is (doall (s/exercise :odm/condition-def 1)))))
