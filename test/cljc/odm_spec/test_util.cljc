(ns odm-spec.test-util
  (:require
    #?(:clj [juxt.iota :refer [given]]
       :cljs [juxt.iota :refer-macros [given]])))

(defn- cljs-env?
  "Take the &env from a macro, and tell whether we are expanding into cljs."
  [env]
  (boolean (:ns env)))

(defmacro if-cljs
  "Return then if we are generating cljs code and else for Clojure code.
   https://groups.google.com/d/msg/clojurescript/iBY5HaQda4A/w1lAQi9_AwsJ"
  [then else]
  (if (cljs-env? &env) then else))

(defmacro given-problems [spec val & body]
  `(if-cljs
     (given (:cljs.spec/problems (cljs.spec/explain-data ~spec ~val))
       ~@body)
     (given (:clojure.spec/problems (clojure.spec/explain-data ~spec ~val))
       ~@body)))
