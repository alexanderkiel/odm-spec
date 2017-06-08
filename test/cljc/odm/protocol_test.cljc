(ns odm.protocol-test
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
         [odm.protocol]))

(st/instrument)

(deftest protocol-test
  (testing "Valid protocols"
    (are [x] (s/valid? :odm/protocol x)
      {:odm/description
       [{:lang-tag "de" :text "foo"}]}

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
      [first :path] := [:odm.protocol/study-event-refs]
      [first :pred] := 'coll?))

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
      [first :path] := [:odm.protocol/study-event-refs]
      [first :pred] := '(partial distinct-values? :odm.study-event-ref/study-event-oid)))

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
      [first :path] := [:odm.protocol/study-event-refs]
      [first :pred] := '(distinct-order-numbers? %)))

  (testing "Invalid aliases"
    (given-problems :odm/protocol
      {:odm/aliases 1}
      [first :path] := [:odm/aliases]
      [first :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/protocol 1)))))
