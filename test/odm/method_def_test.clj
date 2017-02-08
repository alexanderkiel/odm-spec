(ns odm.method-def-test
  (:require [clojure.pprint :refer [pprint]]
            [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.method-def]
            [odm-spec.test-util :refer [given-problems]]))

(deftest method-def-test
  (testing "Valid method definitions"
    (are [x] (s/valid? :odm/method-def x)
      #:odm.method-def
          {:oid "M01"
           :name "foo"
           :type :other
           :odm/description
           {:default "bar"}}))

  (testing "Generator available"
    (is (doall (s/exercise :odm/method-def 1)))))

(comment
  (pprint (map first (s/exercise :odm/method-def 2)))
  )
