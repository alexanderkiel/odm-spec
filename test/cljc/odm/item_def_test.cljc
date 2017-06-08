(ns odm.item-def-test
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
         [odm.item-def]))

(st/instrument)

(deftest item-def-test
  (testing "Valid item definitions"
    (are [x] (s/valid? :odm/item-def x)
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer}

      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :float}

      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :float
           :length 100
           :significant-digits 16}

      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :string
           :length 2000}))

  (testing "Invalid data type"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :foo}
      [first :path] := [:odm.item-def/data-type]
      [first :pred] := #{:text :integer :float :date :time :date-time :string
                     :boolean :double :hex-binary :base64-binary :hex-float
                     :base64-float :partial-date :partial-time
                     :partial-date-time :duration-date-time :interval-date-time
                     :incomplete-date-time :incomplete-date :incomplete-time
                     :uri}))

  (testing "Invalid length"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :length 0}
      [first :path] := [:odm.item-def/length]
      [first :pred] := 'pos?))

  (testing "Missing length"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :text}
      [first :path] := []
      [first :pred] := 'length-given-on-text-or-string?)

    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :string}
      [first :path] := []
      [first :pred] := 'length-given-on-text-or-string?))

  (testing "Invalid significant digits"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :significant-digits -1}
      [first :path] := [:odm.item-def/significant-digits]
      [first :pred] := '(complement neg?)))

  (testing "Given significant digits without length on float"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :float
           :significant-digits 1}
      [first :path] := []
      [first :pred] := 'length-and-significant-digits-given-or-absent-on-float?))

  (testing "Given length without significant digits on float"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :float
           :length 1}
      [first :path] := []
      [first :pred] := 'length-and-significant-digits-given-or-absent-on-float?))

  (testing "Invalid SDS var name"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :sds-var-name ""}
      [first :path] := [:odm.item-def/sds-var-name]))

  (testing "Invalid question"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :question nil}
      [first :path] := [:odm.item-def/question]
      [first :pred] := 'coll?))

  (testing "Duplicate measurement unit ref OIDs"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :measurement-unit-refs
           [#:odm.measurement-unit-ref
               {:measurement-unit-oid "U01"}
            #:odm.measurement-unit-ref
                {:measurement-unit-oid "U01"}]}
      [first :path] := [:odm.item-def/measurement-unit-refs]
      [first :pred] := '(partial distinct-values? :odm.measurement-unit-ref/measurement-unit-oid)))

  (testing "Invalid aliases"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :odm/aliases 1}
      [first :path] := [:odm/aliases]
      [first :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-def 1)))))
