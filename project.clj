(defproject crit-bench "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [criterium "0.4.6"]]
  :java-source-paths ["java"]
  :main crit_bench.Main
  :pom-addition [:properties
                  ["maven.compiler.source" "1.8"]
                  ["maven.compiler.target" "1.8"]]
  :repl-options {:init-ns crit-bench.core})
