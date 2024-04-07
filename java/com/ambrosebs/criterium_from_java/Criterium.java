package com.ambrosebs.criterium_from_java;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class Criterium {

  /**
   * Example benchmark of (* 10 2) in Clojure.
   *
   * Demonstrates how to use bench(Callable) to kick off benchmarking.
   **/
  public static void main(String args[]) {
    System.out.println("Running example benchmark ");
    Map benchResults = quickBench(new Callable<Integer>() {
      public Integer call() throws InterruptedException {
        Thread.sleep(100);
        return 10*2;
      }
    });
    System.out.println("Returns a map of results for programmatic manipulation.");
    System.out.println("For example, benchmark runs returned these values:");
    System.out.println(benchResults.get("results").toString());
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





  //Clojure boilerplate...
  private static Map<Object,Object> runBenchmark(Callable c, Map<Object,Object> config) {
    // if you see reflection warnings, there's a problem and results will not be valid
    eval("(alter-var-root #'*warn-on-reflection* (fn [_] true))");
    IFn runner = (IFn)eval(
        "(fn [^Callable c opts]"+
        "  (let [{:keys [progress quick debug warn] :as opts} (update-keys opts keyword)"+
        "        benchmark (if quick b/quick-benchmark* b/benchmark*)"+
        "        res (with-bindings (cond-> {}"+
        "                             progress (assoc #'b/*report-progress* true)"+
        "                             debug (assoc #'b/*report-debug* true)"+
        "                             warn (assoc #'b/*report-warn* true))"+
        "              (benchmark #(.call c) opts))"+
        "        _ (b/report-result res)"+
        "        stringified (-> (walk/postwalk (fn [v] (cond-> v (map? v) (update-keys name) (sequential? v) vec)) (dissoc res :results))"+
        "                        (assoc \"results\" (vec (:results res))))]"+
        "    (flush)"+ //IMPORTANT!!! results don't get printed otherwise
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
    return evalVar.invoke(read("(clojure.core/binding [clojure.core/*ns* (clojure.core/create-ns 'crit-bench.main)] (clojure.core/eval '(clojure.core/ns crit-bench.main (:require [clojure.walk :as walk] [clojure.pprint :as pp] [criterium.core :as b]) (:import [java.util.concurrent Callable]))) (clojure.core/eval (clojure.core/read-string "+(String)prStrVar.invoke(s)+")))"));
  }
}
