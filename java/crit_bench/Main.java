package crit_bench;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class Main {

  // # Configuration
  // print intermediate benchmark logging?
  public static boolean verboseInProgress = true;
  // print more verbose benchmark summary? such as OS+JDK
  public static boolean verboseResult = true;

  // # Your benchmark
  // replace this method with your benchmark, wrapped in a Runnable.
  // the example implementation benchmarks (* 10 2) in clojure, but
  // makes sure we've compiled and loaded everything non-benchmark related
  // before returning the runnable (which you should do too).
  public static Runnable myBenchmark() {
    IFn cljBenchmark = (IFn)eval("(clojure.core/fn [] (clojure.core/* 10 2))");
    return new Runnable() {
      public void run() {
        // call benchmark here
        cljBenchmark.invoke();
      }
    };
  }

  // boilerplate
  public static void main(String args[]) {
    // boilerplate loading up criterium
    IFn require = Clojure.var("clojure.core", "require");
    require.invoke(Clojure.read("criterium.core"));
    IFn benchmark = Clojure.var("criterium.core", "benchmark*");
    IFn reportResult = Clojure.var("criterium.core", "report-result");
    // if you see reflection warnings, there's a problem and results will not be valid
    eval("(clojure.core/alter-var-root #'clojure.core/*warn-on-reflection* (clojure.core/fn [_] true))");

    System.out.println("Setting up the benchmark");

    //boilerplate kicking off the benchmark and reporting results
    System.out.println("Ready to run!");
    System.out.println("Running...");
    runBenchmark(myBenchmark());
    System.out.println("Done!");
  }

  public static Object read(String s) {
    IFn readStringVar = Clojure.var("clojure.core", "read-string");
    return readStringVar.invoke(s);
  }

  public static Object eval(String s) {
    IFn evalVar = Clojure.var("clojure.core", "eval");
    IFn readStringVar = Clojure.var("clojure.core", "read-string");
    return evalVar.invoke(readStringVar.invoke(s));
  }

  //boilerplate wrapping an IFn
  public static IFn runnableToIFn(Runnable r) {
    return (IFn)eval("(clojure.core/fn [^java.lang.Runnable r] (.run r))");
  }

  public static void runBenchmark(Runnable r) {
    IFn runner = (IFn)eval("(clojure.core/fn [progress? verbose? ^java.lang.Runnable r] (clojure.core/cond-> (criterium.core/benchmark (.run r) {:verbose verbose?}) progress? criterium.core/with-progress-reporting))");
    runner.invoke(verboseInProgress, verboseResult, r);
  }
}
