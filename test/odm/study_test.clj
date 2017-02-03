(ns odm.study-test
  (:require [clojure.spec :as s]
            [clojure.test :refer :all]
            [odm.study]
            [juxt.iota :refer [given]]))

(deftest oid-test
  (is (s/valid? :odm.study/oid "S001")))
