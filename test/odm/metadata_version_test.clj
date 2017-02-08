(ns odm.metadata-version-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.metadata-version]
            [odm-spec.test-util :refer [given-problems]]))

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
                :repeating false}]
           :item-group-defs
           [#:odm.item-group-def
               {:oid "IG01"
                :name "foo"
                :repeating false}]
           :item-defs
           [#:odm.item-def
               {:oid "I01"
                :name "foo"
                :data-type :integer}]}))

  (testing "Missing keys"
    (given-problems :odm/metadata-version
      {}
      [0 :pred] := '(contains? % :odm.metadata-version/oid)
      [1 :pred] := '(contains? % :odm.metadata-version/name)))

  (testing "Invalid description"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :description 1}
      [0 :path] := [:odm.metadata-version/description]
      [0 :pred] := 'string?))

  (testing "Invalid include"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :description "bar"
           :odm/include nil}
      [0 :path] := [:odm/include]
      [0 :pred] := 'map?))

  (testing "Invalid protocol"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :description "bar"
           :odm/protocol nil}
      [0 :path] := [:odm/protocol]
      [0 :pred] := 'map?))

  (testing "Duplicate study event definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :study-event-defs
           [#:odm.study-event-def
               {:oid "SE01"
                :name "foo"
                :repeating false
                :type :common}
            #:odm.study-event-def
                {:oid "SE01"
                 :name "foo"
                 :repeating false
                 :type :common}]}
      [0 :path] := [:odm.metadata-version/study-event-defs]
      [0 :pred] := '(partial distinct-oids? :odm.study-event-def/oid)))

  (testing "Duplicate form definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :form-defs
           [#:odm.form-def
               {:oid "F01"
                :name "foo"
                :repeating false}
            #:odm.form-def
                {:oid "F01"
                 :name "foo"
                 :repeating false}]}
      [0 :path] := [:odm.metadata-version/form-defs]
      [0 :pred] := '(partial distinct-oids? :odm.form-def/oid)))

  (testing "Duplicate item group definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :item-group-defs
           [#:odm.item-group-def
               {:oid "IG01"
                :name "foo"
                :repeating false}
            #:odm.item-group-def
                {:oid "IG01"
                 :name "foo"
                 :repeating false}]}
      [0 :path] := [:odm.metadata-version/item-group-defs]
      [0 :pred] := '(partial distinct-oids? :odm.item-group-def/oid)))

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
      [0 :path] := [:odm.metadata-version/item-defs]
      [0 :pred] := '(partial distinct-oids? :odm.item-def/oid)))

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
                [#:odm.code-list
                    {:coded-value "1"
                     :decode {:default "yes"}}]}
            #:odm.code-list
                {:oid "CL01"
                 :name "foo"
                 :data-type :text
                 :code-list-items
                 [#:odm.code-list
                     {:coded-value "1"
                      :decode {:default "yes"}}]}]}
      [0 :path] := [:odm.metadata-version/code-lists]
      [0 :pred] := '(partial distinct-oids? :odm.code-list/oid)))

  (testing "Duplicate condition definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :condition-defs
           [#:odm.condition-def
               {:oid "C01"
                :name "foo"
                :odm/description {:default "bar"}}
            #:odm.condition-def
                {:oid "C01"
                 :name "foo"
                 :odm/description {:default "bar"}}]}
      [0 :path] := [:odm.metadata-version/condition-defs]
      [0 :pred] := '(partial distinct-oids? :odm.condition-def/oid)))

  (testing "Inconsistent method definition OIDs"
    (given-problems :odm/metadata-version
      #:odm.metadata-version
          {:oid "V01"
           :name "foo"
           :method-defs
           [#:odm.method-def
               {:oid "M01"
                :name "foo"
                :odm/description {:default "bar"}}
            #:odm.method-def
                {:oid "M01"
                 :name "foo"
                 :odm/description {:default "bar"}}]}
      [0 :path] := [:odm.metadata-version/method-defs]
      [0 :pred] := '(partial distinct-oids? :odm.method-def/oid)))

  (testing "Generator available"
    (is (doall (s/exercise :odm/metadata-version 1)))))
