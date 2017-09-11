(ns odm.form-ref-test
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
         [odm.form-ref]))

(st/instrument)

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
      [first :pred] := `(fn [~'%] (contains? ~'% :odm.form-ref/form-oid))
      [second :pred] := `(fn [~'%] (contains? ~'% :odm/mandatory))))

  (testing "Invalid collection exception condition OID"
    (given-problems :odm/form-ref
      #:odm.form-ref
          {:form-oid "F01"
           :odm/mandatory true
           :odm.ref/collection-exception-condition-oid nil}
      [first :path] := [:odm.ref/collection-exception-condition-oid]
      [first :pred] := `string?))

  #?(:clj
     (testing "Generator available"
       (is (doall (s/exercise :odm/form-ref 1))))))
