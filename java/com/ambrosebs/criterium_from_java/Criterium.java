package com.ambrosebs.criterium_from_java;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class Criterium {

  /**
   * Example benchmark of (* 10 2) in Clojure.
   *
   * Demonstrates how to use bench(Runnable) to kick off benchmarking.
   **/
  public static void main(String args[]) {
    System.out.println("Running example benchmark ");
    final IFn cljBenchmark = (IFn)eval("(fn [] (* 10 2))");
    bench(new Runnable() {
      public void run() {
        // call benchmark here
        cljBenchmark.invoke();
      }
    });
  }

  /**
   * Run criterium benchmarking on provided benchmark with verbose printing.
   *
   * @r a runnable benchmark
   **/
  public static void bench(Runnable r) {
    bench(r, true, "{:verbose true}");
  }

  // # Configuration
  // @verboseInProgress if true, print in-progress results of the benchmarking
  // benchmark configuration map
  // :verbose = more verbose benchmark summary, such as OS+JDK
  //
  // default config:
  //   {:max-gc-attempts 100
  //    :samples 60
  //    :target-execution-time 1000000000 ;; in ns
  //    :warmup-jit-period 10000000000 ;; in ns
  //    :tail-quantile 0.025
  //    :bootstrap-size 1000}
  public static void bench(Runnable r, boolean verboseInProgress, String ednConfig) {
    // if you see reflection warnings, there's a problem and results will not be valid
    eval("(alter-var-root #'*warn-on-reflection* (fn [_] true))");

    System.out.println("Setting up the benchmark");

    //boilerplate kicking off the benchmark and reporting results
    System.out.println("Ready to run!");
    System.out.println("Running...");
    runBenchmark(r, verboseInProgress, ednConfig);
    System.out.println("Done!");
  }

  public static Object read(String s) {
    IFn evalVar = Clojure.var("clojure.core", "eval");
    IFn readStringVar = Clojure.var("clojure.core", "read-string");
    IFn readIn = (IFn)evalVar.invoke(readStringVar.invoke("(clojure.core/fn [s] (clojure.core/binding [*ns* (clojure.core/create-ns 'crit-bench.main)] (clojure.core/read-string s)))"));
    return readIn.invoke(s);
  }

  public static Object eval(String s) {
    IFn evalVar = Clojure.var("clojure.core", "eval");
    return evalVar.invoke(read("(clojure.core/binding [clojure.core/*ns* (clojure.core/create-ns 'crit-bench.main)] (clojure.core/eval '(clojure.core/ns crit-bench.main (:require [criterium.core :as b]))) (clojure.core/eval (clojure.core/read-string \""+s+"\")))"));
  }

  public static IFn runnableToIFn(Runnable r) {
    return (IFn)eval("(fn [^Runnable r] (.run r))");
  }

  public static void runBenchmark(Runnable r, boolean verboseInProgress, String ednConfig) {
    IFn runner = (IFn)eval("(fn [progress? opts ^java.lang.Runnable r] (b/report-result (let [f #(b/benchmark (.run r) opts)] (if progress? (b/with-progress-reporting (f)) (f))) (if (:verbose opts) :verbose)))");
    runner.invoke(verboseInProgress, read(ednConfig), r);
  }
}
