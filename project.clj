(defproject com.ambrosebs/criterium-from-java "1.0.0-SNAPSHOT"
  ;; make sure version is synced with test project
  :description "Running Clojure's criterium from other JVM languages."
  :url "https://github.com/frenchy64/criterium-from-java"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [criterium "0.4.6"]]
  :java-source-paths ["java"]
  :main com.ambrosebs.criterium_from_java.Criterium
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :pom-addition [:properties
                  ["maven.compiler.source" "1.8"]
                  ["maven.compiler.target" "1.8"]]
  :repl-options {:init-ns crit-bench.core})
