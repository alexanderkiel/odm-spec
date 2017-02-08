(ns odm-spec.test-util
  (:require [clojure.spec :as s]
            [juxt.iota :refer [given]]))

(defmacro given-problems [spec val & body]
  `(given (::s/problems (s/explain-data ~spec ~val))
     ~@body))
