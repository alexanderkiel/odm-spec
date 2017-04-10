(ns odm.description-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.description]
            [odm-spec.test-util :refer [given-problems]]))

(deftest description-test
  (testing "Valid descriptions"
    (are [x] (s/valid? :odm/description x)
      [{:lang :default :text "foo"}]))

  (testing "Invalid description key"
    (given-problems (s/keys :req [:odm/description])
      {:odm/description nil}
      [0 :path] := [:odm/description]
      [0 :pred] := 'coll?))

  (testing "Generator available"
    (is (doall (s/exercise :odm/description 1)))))
