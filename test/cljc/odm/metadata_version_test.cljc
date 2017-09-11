(ns odm.metadata-version-test
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
         [odm.metadata-version]
         [odm-spec.util :as u]))

(st/instrument)

(deftest metadata-version-test
  (testing "Valid metadata versions"
    (are [x] (s/valid? :odm/metadata-version x)
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"}

      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :include
           #:odm.metadata-version.include
               {:study-oid "S01"
                :metadata-version-oid "V01"}
           :form-defs
           [#:odm.form-def
               {:oid "F01"
                :name "foo"
                :odm.def/repeating false}]
           :item-group-defs
           [#:odm.item-group-def
               {:oid "IG01"
                :name "foo"
                :odm.def/repeating false}]
           :item-defs
           [#:odm.item-def
               {:oid "I01"
                :name "foo"
                :data-type :integer}]}))

  (testing "Missing keys"
    (given-problems :odm/metadata-version
      {}
      [first :pred] := `(fn [~'%] (contains? ~'% :odm.metadata-version/oid))
      [second :pred] := `(fn [~'%] (contains? ~'% :odm.metadata-version/name))))

  (testing "Invalid description"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :description 1}
      [first :path] := [:odm.metadata-version/description]
      [first :pred] := `string?))

  (testing "Invalid include"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :description "bar"
           :odm/include nil}
      [first :path] := [:odm/include]
      [first :pred] := 'map?))

  (testing "Invalid protocol"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :description "bar"
           :odm/protocol nil}
      [first :path] := [:odm/protocol]
      [first :pred] := 'map?))

  (testing "Duplicate study event definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :study-event-defs
           [#:odm.study-event-def
               {:oid "SE01"
                :name "foo"
                :odm.def/repeating false
                :type :common}
            #:odm.study-event-def
                {:oid "SE01"
                 :name "foo"
                 :odm.def/repeating false
                 :type :common}]}
      [first :path] := [:odm.metadata-version/study-event-defs]
      [first :pred] := `(partial u/distinct-values? :odm.study-event-def/oid)))

  (testing "Duplicate form definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :form-defs
           [#:odm.form-def
               {:oid "F01"
                :name "foo"
                :odm.def/repeating false}
            #:odm.form-def
                {:oid "F01"
                 :name "foo"
                 :odm.def/repeating false}]}
      [first :path] := [:odm.metadata-version/form-defs]
      [first :pred] := `(partial u/distinct-values? :odm.form-def/oid)))

  (testing "Duplicate item group definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :item-group-defs
           [#:odm.item-group-def
               {:oid "IG01"
                :name "foo"
                :odm.def/repeating false}
            #:odm.item-group-def
                {:oid "IG01"
                 :name "foo"
                 :odm.def/repeating false}]}
      [first :path] := [:odm.metadata-version/item-group-defs]
      [first :pred] := `(partial u/distinct-values? :odm.item-group-def/oid)))

  (testing "Duplicate item definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :item-defs
           [#:odm.item-def
               {:oid "I01"
                :name "foo"
                :data-type :integer}
            #:odm.item-def
                {:oid "I01"
                 :name "foo"
                 :data-type :integer}]}
      [first :path] := [:odm.metadata-version/item-defs]
      [first :pred] := `(partial u/distinct-values? :odm.item-def/oid)))

  (testing "Duplicate code list OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :code-lists
           [#:odm.code-list
               {:oid "CL01"
                :name "foo"
                :data-type :text
                :code-list-items
                [#:odm.code-list-item
                    {:coded-value "1"
                     :decode [{:lang-tag "de" :text "yes"}]}]}
            #:odm.code-list
                {:oid "CL01"
                 :name "foo"
                 :data-type :text
                 :code-list-items
                 [#:odm.code-list-item
                     {:coded-value "1"
                      :decode [{:lang-tag "de" :text "yes"}]}]}]}
      [first :path] := [:odm.metadata-version/code-lists]
      [first :pred] := `(partial u/distinct-values? :odm.code-list/oid)))

  (testing "Duplicate condition definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :condition-defs
           [#:odm.condition-def
               {:oid "C01"
                :name "foo"
                :odm/description [{:lang-tag "de" :text "bar"}]}
            #:odm.condition-def
                {:oid "C01"
                 :name "foo"
                 :odm/description [{:lang-tag "de" :text "bar"}]}]}
      [first :path] := [:odm.metadata-version/condition-defs]
      [first :pred] := `(partial u/distinct-values? :odm.condition-def/oid)))

  (testing "Inconsistent method definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :method-defs
           [#:odm.method-def
               {:oid "M01"
                :name "foo"
                :odm/description [{:lang-tag "de" :text "bar"}]}
            #:odm.method-def
                {:oid "M01"
                 :name "foo"
                 :odm/description [{:lang-tag "de" :text "bar"}]}]}
      [first :path] := [:odm.metadata-version/method-defs]
      [first :pred] := `(partial u/distinct-values? :odm.method-def/oid)))

  #?(:clj
     (testing "Generator available"
       (is (doall (s/exercise :odm/metadata-version 1))))))
