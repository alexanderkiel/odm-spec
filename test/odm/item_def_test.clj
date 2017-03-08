(ns odm.item-def-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.item-def]
            [odm-spec.test-util :refer [given-problems]]))

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
      [0 :path] := [:odm.item-def/data-type]
      [0 :pred] := #{:text :integer :float :date :time :date-time :string
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
      [0 :path] := [:odm.item-def/length]
      [0 :pred] := 'pos?))

  (testing "Missing length"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :text}
      [0 :path] := []
      [0 :pred] := 'length-given-on-text-or-string?)

    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :string}
      [0 :path] := []
      [0 :pred] := 'length-given-on-text-or-string?))

  (testing "Invalid significant digits"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :significant-digits -1}
      [0 :path] := [:odm.item-def/significant-digits]
      [0 :pred] := '(complement neg?)))

  (testing "Given significant digits without length on float"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :float
           :significant-digits 1}
      [0 :path] := []
      [0 :pred] := 'length-and-significant-digits-given-or-absent-on-float?))

  (testing "Given length without significant digits on float"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :float
           :length 1}
      [0 :path] := []
      [0 :pred] := 'length-and-significant-digits-given-or-absent-on-float?))

  (testing "Invalid SDS var name"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :sds-var-name ""}
      [0 :path] := [:odm.item-def/sds-var-name]))

  (testing "Invalid question"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :question nil}
      [0 :path] := [:odm.item-def/question]
      [0 :pred] := 'map?))

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
      [0 :path] := [:odm.item-def/measurement-unit-refs]
      [0 :pred] := '(partial distinct-oids? :odm.measurement-unit-ref/measurement-unit-oid)))

  (testing "Invalid aliases"
    (given-problems :odm/item-def
      #:odm.item-def
          {:oid "I01"
           :name "foo"
           :data-type :integer
           :odm/aliases 1}
      [0 :path] := [:odm/aliases]
      [0 :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/item-def 1)))))

(comment
  (clojure.pprint/pprint (map first (s/exercise :odm/item-def 2)))
  )
