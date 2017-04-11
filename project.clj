(defproject org.clojars.akiel/odm-spec "0.4-SNAPSHOT"
  :description "Clojure specs for ODM data structures."
  :url "https://github.com/alexanderkiel/odm-spec"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0-alpha15"]]

  :profiles {:dev
             {:dependencies [[criterium "0.4.4"]
                             [juxt/iota "0.2.2"]
                             [org.clojure/test.check "0.9.0"]]}})
