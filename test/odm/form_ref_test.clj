(ns odm.form-ref-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.form-ref]
            [odm-spec.test-util :refer [given-problems]]))

(deftest form-ref-test
  (testing "Valid form references"
    (are [x] (s/valid? :odm/form-ref x)
      #:odm.form-ref
          {:form-oid "F01"
           :odm/mandatory true}

      #:odm.form-ref
          {:form-oid "F01"
           :odm/order-number 1
           :odm/mandatory true}))

  (testing "Missing keys"
    (given-problems :odm/form-ref
      {}
      [0 :pred] := '(contains? % :odm.form-ref/form-oid)
      [1 :pred] := '(contains? % :odm/mandatory)))

  (testing "Invalid collection exception condition OID"
    (given-problems :odm/form-ref
      #:odm.form-ref
          {:form-oid "F01"
           :odm/mandatory true
           :odm.ref/collection-exception-condition-oid nil}
      [0 :path] := [:odm.ref/collection-exception-condition-oid]
      [0 :pred] := 'string?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/form-ref 1)))))
