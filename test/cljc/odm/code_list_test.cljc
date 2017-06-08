(ns odm.code-list-test
  (:require
    #?@(:clj
        [[clojure.spec :as s]
         [clojure.spec.test :as st]
         [clojure.test :refer :all]
         [odm-spec.test-util :refer [given-problems]]]
        :cljs
        [[cljs.spec :as s]
         [cljs.spec.test :as st]
         [cljs.test :refer-macros [deftest testing is are]]
         [odm-spec.test-util :refer-macros [given-problems]]])
         [odm.code-list]
         [odm.code-list-item :as code-list-item]
         [odm.enumerated-item :as enumerated-item]))

(st/instrument)

(deftest code-list-test
  (testing "Valid code lists"
    (are [x] (s/valid? :odm/code-list x)
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#::code-list-item
               {:coded-value "1"
                :decode [{:lang-tag "de" :text "yes"}]}]}

      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#::code-list-item
               {:coded-value "1"
                :decode [{:lang-tag "de" :text "yes"}]}
            #::code-list-item
                {:coded-value "2"
                 :decode [{:lang-tag "de" :text "yes"}]}]}

      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#::code-list-item
               {:coded-value "1"
                :rank 1
                :decode [{:lang-tag "de" :text "yes"}]}
            #::code-list-item
                {:coded-value "2"
                 :rank 2
                 :decode [{:lang-tag "de" :text "yes"}]}]}

      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#::code-list-item
               {:coded-value "1"
                :odm/order-number 1
                :decode [{:lang-tag "de" :text "yes"}]}
            #::code-list-item
                {:coded-value "2"
                 :odm/order-number 2
                 :decode [{:lang-tag "de" :text "yes"}]}]}))

  (testing "Missing code list and enumerated item"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer}
      [first :path] := []
      [first :pred] := '(or (contains? % :odm.code-list/code-list-items)
                            (contains? % :odm.code-list/enumerated-items))))

  (testing "Both code list and enumerated item present"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#::code-list-item
               {:coded-value "1"
                :decode [{:lang-tag "de" :text "yes"}]}]
           :enumerated-items
           [#:odm.enumerated-item
               {:coded-value "2"
                :decode [{:lang-tag "de" :text "no"}]}]}
      [first :path] := []
      [first :pred] := '(not (and (contains? % :odm.code-list/code-list-items)
                                  (contains? % :odm.code-list/enumerated-items)))))

  (testing "Duplicate coded values on code list items"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#::code-list-item
               {:coded-value "1"
                :decode [{:lang-tag "de" :text "yes"}]}
            #::code-list-item
                {:coded-value "1"
                 :decode [{:lang-tag "de" :text "no"}]}]}
      [first :path] := [:odm.code-list/code-list-items]
      [first :pred] := '(distinct-values? ::code-list-item/coded-value %)))

  (testing "Duplicate ranks on code list items"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#::code-list-item
               {:coded-value "1"
                :rank 0
                :decode [{:lang-tag "de" :text "yes"}]}
            #::code-list-item
                {:coded-value "2"
                 :rank 0
                 :decode [{:lang-tag "de" :text "no"}]}]}
      [first :path] := [:odm.code-list/code-list-items]
      [first :pred] := '(distinct-or-no-values? ::code-list-item/rank %)))

  (testing "Duplicate order numbers on code list items"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#::code-list-item
               {:coded-value "1"
                :odm/order-number 1
                :decode [{:lang-tag "de" :text "yes"}]}
            #::code-list-item
                {:coded-value "2"
                 :odm/order-number 1
                 :decode [{:lang-tag "de" :text "no"}]}]}
      [first :path] := [:odm.code-list/code-list-items]
      [first :pred] := '(distinct-or-no-values? :odm/order-number %)))

  (testing "Duplicate coded values on enumerated items"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :enumerated-items
           [#::enumerated-item{:coded-value "1"}
            #::enumerated-item{:coded-value "1"}]}
      [first :path] := [:odm.code-list/enumerated-items]
      [first :pred] := '(distinct-values? ::enumerated-item/coded-value %)))

  (testing "Duplicate ranks on enumerated items"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :enumerated-items
           [#::enumerated-item
               {:coded-value "1"
                :rank 0}
            #::enumerated-item
                {:coded-value "2"
                 :rank 0}]}
      [first :path] := [:odm.code-list/enumerated-items]
      [first :pred] := '(distinct-or-no-values? ::enumerated-item/rank %)))

  (testing "Duplicate order numbers on enumerated items"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :enumerated-items
           [#::enumerated-item
               {:coded-value "1"
                :odm/order-number 1}
            #::enumerated-item
                {:coded-value "2"
                 :odm/order-number 1}]}
      [first :path] := [:odm.code-list/enumerated-items]
      [first :pred] := '(distinct-or-no-values? :odm/order-number %)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/code-list 1)))))
