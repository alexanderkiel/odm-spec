(ns odm.alias-test
  (:require
    #?@(:clj
        [[clojure.spec :as s]
         [clojure.spec.test :as st]
         [clojure.test :refer :all]]
        :cljs
        [[cljs.spec :as s]
         [cljs.spec.test :as st]
         [cljs.test :refer-macros [deftest testing is are]]])
         [odm.alias]))

(st/instrument)

(deftest alias-test
  (testing "Valid alias"
    (are [x] (s/valid? :odm/alias x)
      #:odm.alias
          {:context "app"
           :name "a"}))

  (testing "Generator available"
    (is (doall (s/exercise :odm/alias 1)))))
