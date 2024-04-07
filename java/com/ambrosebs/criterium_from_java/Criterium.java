package com.ambrosebs.criterium_from_java;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Input configuration:
 *
 * "quick"  - if true, run a less rigorous benchmark (higher uncertainty)
 * "debug"  - if true, enable criterium debug printing
 * "warn"   - if true, enable criterium warning printing
 * "max-gc-attempts"  - default 100
 * "samples" - default 60, 6 for quick benchmarks
 * "target-execution-time" - default 100000000
 * "warmup-jit-period" - default 5000000000
 * "tail-quantile" - default 0.025
 * "bootstrap-size" - default 1000, 500 for quick benchmarks
 * "overhead" - If you want consistency across JVM processes, set this to a constant value.
 * "final-gc-problem-threshold" - Fraction of execution time allowed for final cleanup before a warning is issued. Default: 0.01.
 * "use-mxbean-for-times" - Default false
 *
 * Output results:
 *
 * "os-details" - a Map of "arch", "available-processors", "name", "version", sourced from java.lang.management.ManagementFactory.getOperatingSystemMXBean()
 * "execution-count" - a Long
 * "options" - the options used for this run, including the defaults. can be passed back as config to bench(Callable, Map) to repeat benchmark.
 * "serialized-options" - the options used for this run as a serialized string. can be passed back as config to bench(Callable, String) to repeat benchmark.
 * "sample-mean" - a Double
 * "mean" - a Long
 * "tail-quantile" - a Double
 * "samples"
 * "variance"
 * "sample-variance"
 * "lower-q"
 * "upper-q"
 * "outlier-variance"
 * "runtime-details"
 **/
public class Criterium {

  /**
   * Example benchmark of (* 10 2) in Clojure.
   *
   * Demonstrates how to use bench(Callable) to kick off benchmarking.
   **/
  public static void main(String args[]) {
    System.out.println("Running example benchmark ");
    Callable<Integer> myBench = new Callable<Integer>() {
      public Integer call() throws InterruptedException {
        Thread.sleep(100);
        return 10*2;
      }
    };
    Map benchResults = quickBench(myBench);
    System.out.println("\nReturns a map of results for programmatic manipulation.");
    System.out.println("For example, benchmark run 5 return this value:");
    List results = (List)benchResults.get("results");
    System.out.println(results.get(4).toString());

    System.out.println("\nWe can reproduce the same benchmark by copy-pasting the above serialized config string into the Java program.");
    bench(myBench,
        "{\"quick\" true, \"max-gc-attempts\" 100, \"samples\" 6, \"target-execution-time\" 100000000, \"warmup-jit-period\" 5000000000, \"tail-quantile\" 0.025, \"bootstrap-size\" 500, \"overhead\" 8.253210752367998E-9}");
  }

  /**
   * Run criterium quick benchmarking on provided benchmark with verbose printing.
   *
   * @r a runnable benchmark
   **/
  public static Map<Object,Object> quickBench(Callable r) {
    final Map<Object,Object> config = new HashMap<Object,Object>();
    config.put("quick", true);
    return bench(r, config);
  }

  /**
   * Run criterium benchmarking on provided benchmark with verbose printing.
   *
   * @r a runnable benchmark
   **/
  public static Map<Object,Object> bench(Callable r) {
    final Map<Object,Object> config = new HashMap<Object,Object>();
    //config.put("verbose", true);
    //config.put("progress", true);
    return bench(r, config);
  }

  // # Configuration
  // @verboseInProgress if true, print in-progress results of the benchmarking
  // benchmark configuration map
  // :verbose = more verbose benchmark summary, such as OS+JDK
  //
  // default config:
  //   {"max-gc-attempts" 100
  //    "samples" 60
  //    "target-execution-time" 1000000000 ;; in ns
  //    "warmup-jit-period" 10000000000 ;; in ns
  //    "tail-quantile" 0.025
  //    "bootstrap-size" 1000}
  public static Map<Object,Object> bench(Callable r, Map<Object,Object> config) {
    return runBenchmark(r, config);
  }

  public static Map<Object,Object> bench(Callable r, String config) {
    return runBenchmark(r, config);
  }




  //Clojure boilerplate...
  private static Map<Object,Object> runBenchmark(Callable c, Object config) {
    // if you see reflection warnings, there's a problem and results will not be valid
    eval("(alter-var-root #'*warn-on-reflection* (fn [_] true))");
    IFn runner = (IFn)eval(
        "(fn [^Callable c s-opts]"+
        "  (let [s-opts (if (string? s-opts) (edn/read-string s-opts) (into {} s-opts))"+
        "        {:keys [progress quick debug warn max-gc-attempts final-gc-problem-threshold] :as opts} (update-keys s-opts keyword)"+
        "        benchmark (if quick b/quick-benchmark* b/benchmark*)"+
        "        res (with-bindings (cond-> {}"+
        "                             progress (assoc #'b/*report-progress* true)"+
        "                             debug (assoc #'b/*report-debug* true)"+
        "                             max-gc-attempts (assoc #'b/*max-gc-attempts* max-gc-attempts)"+
        "                             final-gc-problem-threshold (assoc #'b/*final-gc-problem-threshold* final-gc-problem-threshold)"+
        "                             warn (assoc #'b/*report-warn* true))"+
        "              (benchmark #(.call c) (dissoc opts :progress :quick :debug :warn :final-gc-problem-threshold)))"+
        "        _ (b/report-result res)"+
        "        {:strs [options] :as stringified} (-> (walk/postwalk (fn [v] (cond-> v (map? v) (update-keys name) (sequential? v) vec)) (dissoc res :results))"+
        "                                              (assoc \"results\" (vec (:results res)))"+
        "                                              (update \"options\" #(into s-opts %)))"+
        "        {:strs [serialized-options] :as stringified} (assoc stringified \"serialized-options\" (pr-str options))]"+
        "    (flush)"+ //IMPORTANT!!! results don't get printed otherwise
        "    (println \"To reproduce via bench(Callable, String), use:\" (pr-str serialized-options))"+
        "    stringified))");
    return (Map)runner.invoke(c, config);
  }

  private static Object read(String s) {
    IFn evalVar = Clojure.var("clojure.core", "eval");
    IFn readStringVar = Clojure.var("clojure.core", "read-string");
    IFn readIn = (IFn)evalVar.invoke(readStringVar.invoke("(clojure.core/fn [s] (clojure.core/binding [clojure.core/*ns* (clojure.core/create-ns 'crit-bench.main)] (clojure.core/read-string s)))"));
    return readIn.invoke(s);
  }

  private static Object eval(String s) {
    IFn evalVar = Clojure.var("clojure.core", "eval");
    IFn prStrVar = Clojure.var("clojure.core", "pr-str");
    return evalVar.invoke(read("(clojure.core/binding [clojure.core/*ns* (clojure.core/create-ns 'crit-bench.main)] (clojure.core/eval '(clojure.core/ns crit-bench.main (:require [clojure.edn :as edn] [clojure.walk :as walk] [criterium.core :as b]) (:import [java.util.concurrent Callable]))) (clojure.core/eval (clojure.core/read-string "+(String)prStrVar.invoke(s)+")))"));
  }
}
