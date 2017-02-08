(ns odm.protocol-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.protocol]
            [odm-spec.test-util :refer [given-problems]]))

(deftest protocol-test
  (testing "Valid protocols"
    (are [x] (s/valid? :odm/protocol x)
      {:odm/description
       {:default "foo"}}

      #:odm.protocol
          {:study-event-refs
           [#:odm.study-event-ref
               {:study-event-oid "SE01"
                :odm/mandatory true}]}

      #:odm.protocol
          {:study-event-refs
           [#:odm.study-event-ref
               {:study-event-oid "SE01"
                :odm/mandatory true
                :odm/order-number 1}
            #:odm.study-event-ref
                {:study-event-oid "SE02"
                 :odm/mandatory true
                 :odm/order-number 2}]}))

  (testing "Invalid study event refs"
    (given-problems :odm/protocol
      #:odm.protocol
          {:study-event-refs nil}
      [0 :path] := [:odm.protocol/study-event-refs]
      [0 :pred] := 'coll?))

  (testing "Duplicate study event ref OIDs"
    (given-problems :odm/protocol
      #:odm.protocol
          {:study-event-refs
           [#:odm.study-event-ref
               {:study-event-oid "SE01"
                :odm/mandatory true}
            #:odm.study-event-ref
                {:study-event-oid "SE01"
                 :odm/mandatory true}]}
      [0 :path] := [:odm.protocol/study-event-refs]
      [0 :pred] := '(partial distinct-oids? :odm.study-event-ref/study-event-oid)))

  (testing "Duplicate order numbers in study event refs"
    (given-problems :odm/protocol
      #:odm.protocol
          {:study-event-refs
           [#:odm.study-event-ref
               {:study-event-oid "SE01"
                :odm/mandatory true
                :odm/order-number 1}
            #:odm.study-event-ref
                {:study-event-oid "SE02"
                 :odm/mandatory true
                 :odm/order-number 1}]}
      [0 :path] := [:odm.protocol/study-event-refs]
      [0 :pred] := '(distinct-order-numbers? %)))

  (testing "Invalid aliases"
    (given-problems :odm/protocol
      {:odm/aliases 1}
      [0 :path] := [:odm/aliases]
      [0 :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/protocol 1)))))
