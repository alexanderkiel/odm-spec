(defproject org.clojars.akiel/odm-spec "0.4-SNAPSHOT"
  :description "Clojure specs for ODM data structures."
  :url "https://github.com/alexanderkiel/odm-spec"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.6.0"
  :pedantic? :abort

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-doo "0.1.7"]]

  :profiles
  {:dev
   {:dependencies [[criterium "0.4.4"]
                   [juxt/iota "0.2.2"]
                   [org.clojure/clojure "1.9.0-alpha15"]
                   [org.clojure/clojurescript "1.9.494"]
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
      :optimizations :simple}}}}

  :clean-targets ["target" "out"])
