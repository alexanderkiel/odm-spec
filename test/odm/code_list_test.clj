(ns odm.code-list-test
  (:require [clojure.pprint :refer [pprint]]
            [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.code-list]
            [odm-spec.test-util :refer [given-problems]]))

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
      [0 :path] := []
      [0 :pred] := '(or (contains? % :odm.code-list/code-list-items)
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
      [0 :path] := []
      [0 :pred] := '(not (and (contains? % :odm.code-list/code-list-items)
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
      [0 :path] := [:odm.code-list/code-list-items]
      [0 :pred] := 'distinct-coded-values?))

  (testing "Generator available"
      (is (doall (s/exercise :odm/code-list 1)))))

(comment
  (pprint (map first (s/exercise :odm/code-list 1)))
  )
