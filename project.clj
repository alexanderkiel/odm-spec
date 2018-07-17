(defproject org.clojars.akiel/odm-spec "0.6-SNAPSHOT"
  :description "Clojure specs for ODM data structures."
  :url "https://github.com/alexanderkiel/odm-spec"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.6.0"
  :pedantic? :abort

  :plugins
  [[lein-cljsbuild "1.1.7"]
   [lein-doo "0.1.10" :exclusions [org.clojure/clojure]]]

  :profiles
  {:dev
   {:dependencies
    [[org.clojars.akiel/iota "0.1"]
     [org.clojure/clojure "1.9.0"]
     [org.clojure/clojurescript "1.10.339"]
     [org.clojure/test.check "0.9.0"]]}}

  :source-paths ["src"]
  :test-paths ["test/cljc"]

  :cljsbuild
  {:builds
   {:test
    {:source-paths ["src" "test/cljc" "test/cljs"]
     :compiler
     {:output-to "out/testable.js"
      :main odm-spec.runner
      :optimizations :simple
      :process-shim false
      :closure-defines {"goog.DEBUG" false}}}}}

  :clean-targets ["target" "out"]

  :aliases
  {"cljs-nashorn-tests" ["doo" "nashorn" "test" "once"]
   "cljs-phantom-tests" ["doo" "phantom" "test" "once"]
   "all-tests" ["do" "test," "cljs-nashorn-tests," "cljs-phantom-tests"]})
