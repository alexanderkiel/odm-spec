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
         [odm.code-list]))

(st/instrument)

(deftest code-list-test
  (testing "Valid code lists"
    (are [x] (s/valid? :odm/code-list x)
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#:odm.code-list
               {:coded-value "1"
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
           [#:odm.code-list
               {:coded-value "1"
                :decode [{:lang-tag "de" :text "yes"}]}]
           :enumerated-items
           [#:odm.code-list
               {:coded-value "2"
                :decode [{:lang-tag "de" :text "no"}]}]}
      [first :path] := []
      [first :pred] := '(not (and (contains? % :odm.code-list/code-list-items)
                                  (contains? % :odm.code-list/enumerated-items)))))

  (testing "Duplicate coded values"
    (given-problems :odm/code-list
      #:odm.code-list
          {:oid "CL01"
           :name "foo"
           :data-type :integer
           :code-list-items
           [#:odm.code-list
               {:coded-value "1"
                :decode [{:lang-tag "de" :text "yes"}]}
            #:odm.code-list
                {:coded-value "1"
                 :decode [{:lang-tag "de" :text "no"}]}]}
      [first :path] := [:odm.code-list/code-list-items]
      [first :pred] := 'distinct-coded-values?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/code-list 1)))))
