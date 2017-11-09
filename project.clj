(defproject org.clojars.akiel/odm-spec "0.3-alpha14"
  :description "Clojure specs for ODM data structures."
  :url "https://github.com/alexanderkiel/odm-spec"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0-RC1"]]

  :profiles {:dev
             {:dependencies [[org.clojure/test.check "0.9.0"]
                             [juxt/iota "0.2.2"]]}})
