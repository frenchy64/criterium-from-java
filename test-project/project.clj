(defproject test-project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[com.ambrosebs/criterium-from-java "1.1.1-SNAPSHOT"]]
  :main example_benchmark.Main
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :java-source-paths ["java"])
