# Running Criterium benchmarks from Java and Maven

Clojure's [criterium](https://github.com/hugoduncan/criterium/) is a great library for microbenchmarking on the JVM.
This project packages it up for use from other JVM languages.

## API

There is one public method `Criterium.bench(Callable)`. It takes a runnable that
runs the benchmark. You should initialize your benchmark (if appropriate) before returning the Callable.

```java
package example_benchmark;

import com.ambrosebs.criterium_from_java.Criterium;

public class Main {
  public static void main(String[] args) {
    Criterium.bench(new Callable() {
      @Override
      public void run() {
        java.util.UUID.randomUUID();
        ("string"+"concatenation"+"benchmark").length();
      }
    });
  }
}
```

## Maven Dependency information

```xml
...
    <repository>
      <id>clojars</id>
      <url>https://repo.clojars.org/</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
...
    <dependency>
      <groupId>com.ambrosebs</groupId>
      <artifactId>criterium-from-java</artifactId>
      <version>1.0.0</version>
    </dependency>
...
```


## Example

There is an example benchmark that is run via Maven. The code looks like this:

```
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

    // takes a configuration map as described in Criterium class javadoc.
    final Map<Object,Object> config = new HashMap<Object,Object>();
    config.put("quick", true);
    config.put("print-result", true);
    config.put("verbose", true);
    config.put("progress", true);
    Map benchResults = bench(myBench, config);

    System.out.println("\nReturns a map of results for programmatic manipulation.");
    System.out.println("For example, benchmark run 5 return this value:");
    List results = (List)benchResults.get("results");
    System.out.println(results.get(4).toString());

    System.out.println("\nWe can reproduce the same benchmark by copy-pasting the above serialized config string into the Java program.");

    // also takes Clojure data, useful to reproduce previous results.
    bench(myBench,
        "{\"quick\" true, \"max-gc-attempts\" 100, \"samples\" 6, \"target-execution-time\" 100000000, \"warmup-jit-period\" 5000000000, \"tail-quantile\" 0.025, \"bootstrap-size\" 500, \"overhead\" 8.253210752367998E-9}");
  }
```

Here is some example output:

```
./run.sh
+ mvn clean
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------< com.ambrosebs:criterium-from-java >------------------
[INFO] Building criterium-from-java 1.1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- clean:3.2.0:clean (default-clean) @ criterium-from-java ---
[INFO] Deleting /Users/ambrose/Projects/criterium-from-java/target
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.271 s
[INFO] Finished at: 2024-04-07T01:12:38-05:00
[INFO] ------------------------------------------------------------------------
+ mvn compile
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------< com.ambrosebs:criterium-from-java >------------------
[INFO] Building criterium-from-java 1.1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- build-helper:1.7:add-source (add-source) @ criterium-from-java ---
[INFO] Source directory: /Users/ambrose/Projects/criterium-from-java/java added.
[INFO]
[INFO] --- resources:3.3.1:resources (default-resources) @ criterium-from-java ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 0 resource from resources to target/classes
[INFO]
[INFO] --- compiler:3.11.0:compile (default-compile) @ criterium-from-java ---
[INFO] Changes detected - recompiling the module! :source
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 1 source file with javac [debug target 1.8] to target/classes
[WARNING] bootstrap class path not set in conjunction with -source 8
[WARNING] source value 8 is obsolete and will be removed in a future release
[WARNING] target value 8 is obsolete and will be removed in a future release
[WARNING] To suppress warnings about obsolete options, use -Xlint:-options.
[INFO] /Users/ambrose/Projects/criterium-from-java/java/com/ambrosebs/criterium_from_java/Criterium.java: /Users/ambrose/Projects/criterium-from-java/java/com/ambrosebs/criterium_from_java/Criterium.java uses unchecked or unsafe operations.
[INFO] /Users/ambrose/Projects/criterium-from-java/java/com/ambrosebs/criterium_from_java/Criterium.java: Recompile with -Xlint:unchecked for details.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.330 s
[INFO] Finished at: 2024-04-07T01:12:41-05:00
[INFO] ------------------------------------------------------------------------
+ mvn exec:java -Dexec.mainClass=com.ambrosebs.criterium_from_java.Criterium
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------< com.ambrosebs:criterium-from-java >------------------
[INFO] Building criterium-from-java 1.1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- exec:3.2.0:java (default-cli) @ criterium-from-java ---
Running example benchmark
Estimating sampling overhead
Warming up for JIT optimisations 10000000000 ...
  compilation occurred before 454260 iterations
  compilation occurred before 908441 iterations
  compilation occurred before 1362622 iterations
  compilation occurred before 1816803 iterations
  compilation occurred before 3179346 iterations
  compilation occurred before 5904432 iterations
  compilation occurred before 8629518 iterations
  compilation occurred before 11354604 iterations
  compilation occurred before 11808785 iterations
  compilation occurred before 20438224 iterations
  compilation occurred before 24980034 iterations
  compilation occurred before 25888396 iterations
  compilation occurred before 27705120 iterations
  compilation occurred before 28613482 iterations
  compilation occurred before 34063654 iterations
  compilation occurred before 48143265 iterations
  compilation occurred before 49505808 iterations
  compilation occurred before 56318523 iterations
  compilation occurred before 57681066 iterations
  compilation occurred before 65856324 iterations
  compilation occurred before 73577401 iterations
  compilation occurred before 80390116 iterations
  compilation occurred before 85840288 iterations
  compilation occurred before 86294469 iterations
  compilation occurred before 114453691 iterations
  compilation occurred before 114907872 iterations
  compilation occurred before 115362053 iterations
  compilation occurred before 116270415 iterations
  compilation occurred before 145337999 iterations
  compilation occurred before 148063085 iterations
  compilation occurred before 173951402 iterations
  compilation occurred before 177130669 iterations
  compilation occurred before 177584850 iterations
  compilation occurred before 183035022 iterations
  compilation occurred before 183489203 iterations
  compilation occurred before 190301918 iterations
  compilation occurred before 232086570 iterations
  compilation occurred before 253433077 iterations
  compilation occurred before 257066525 iterations
  compilation occurred before 257520706 iterations
  compilation occurred before 298851177 iterations
  compilation occurred before 299305358 iterations
  compilation occurred before 322468589 iterations
  compilation occurred before 433742934 iterations
  compilation occurred before 448730907 iterations
  compilation occurred before 456451984 iterations
  compilation occurred before 457814527 iterations
  compilation occurred before 498690817 iterations
  compilation occurred before 525487496 iterations
  compilation occurred before 563184519 iterations
  compilation occurred before 609056800 iterations
  compilation occurred before 759390711 iterations
  compilation occurred before 1009190261 iterations
  compilation occurred before 1023269872 iterations
  compilation occurred before 1051429094 iterations
  compilation occurred before 1068687972 iterations
  compilation occurred before 1142265294 iterations
  compilation occurred before 1159524172 iterations
  compilation occurred before 1159978353 iterations
  compilation occurred before 1169516154 iterations
  compilation occurred before 1169970335 iterations
  compilation occurred before 1170424516 iterations
  compilation occurred before 1187229213 iterations
  compilation occurred before 1187683394 iterations
Estimating execution count ...
Sampling ...
Final GC...
Checking GC...
Finding outliers ...
Bootstrapping ...
Checking outlier significance
Warming up for JIT optimisations 5000000000 ...
  compilation occurred before 10 iterations
Estimating execution count ...
Sampling ...
Final GC...
Checking GC...
Finding outliers ...
Bootstrapping ...
Checking outlier significance
Evaluation count : 6 in 6 samples of 1 calls.
             Execution time mean : 103.210107 ms
    Execution time std-deviation : 1.784294 ms
   Execution time lower quantile : 100.184480 ms ( 2.5%)
   Execution time upper quantile : 104.768084 ms (97.5%)
                   Overhead used : 8.451731 ns
To reproduce via bench(Callable, String), use: "{\"progress\" true, \"target-execution-time\" 100000000, \"max-gc-attempts\" 100, \"print-result\" true, \"warmup-jit-period\" 5000000000, \"quick\" true, \"overhead\" 8.451731464212423E-9, \"bootstrap-size\" 500, \"tail-quantile\" 0.025, \"verbose\" true, \"samples\" 6}"

Result:
{"os-details"
 {"arch" "x86_64",
  "available-processors" 8,
  "name" "Mac OS X",
  "version" "14.3"},
 "execution-count" 1,
 "mean" [0.10321010683333333 [0.10135429133333333 0.104280358]],
 "serialized-options"
 "{\"progress\" true, \"target-execution-time\" 100000000, \"max-gc-attempts\" 100, \"print-result\" true, \"warmup-jit-period\" 5000000000, \"quick\" true, \"overhead\" 8.451731464212423E-9, \"bootstrap-size\" 500, \"tail-quantile\" 0.025, \"verbose\" true, \"samples\" 6}",
 "total-time" 0.620001001,
 "upper-q" [0.10476808400000001 [0.10388682562500001 0.104810285]],
 "sample-count" 6,
 "sample-mean"
 [0.10333350016666668 [0.09809736797918299 0.10856963235415036]],
 "results" [20 20 20 20 20 20],
 "outliers"
 {"low-severe" 0, "low-mild" 0, "high-mild" 0, "high-severe" 0},
 "warmup-executions" 55,
 "runtime-details"
 {"input-arguments"
  ["-Dclassworlds.conf=/usr/local/Cellar/maven/3.9.6/libexec/bin/m2.conf"
   "-Dmaven.home=/usr/local/Cellar/maven/3.9.6/libexec"
   "-Dlibrary.jansi.path=/usr/local/Cellar/maven/3.9.6/libexec/lib/jansi-native"
   "-Dmaven.multiModuleProjectDirectory=/Users/ambrose/Projects/criterium-from-java"],
  "java-runtime-version" "21.0.1+12-LTS",
  "clojure-version"
  {"major" 1, "minor" 11, "incremental" 1, "qualifier" nil},
  "vm-vendor" "Eclipse Adoptium",
  "vm-name" "OpenJDK 64-Bit Server VM",
  "spec-name" "Java Virtual Machine Specification",
  "vm-version" "21.0.1+12-LTS",
  "name" "33018@AmbrosesMBP2020.lan",
  "java-version" "21.0.1",
  "spec-vendor" "Oracle Corporation",
  "sun-arch-data-model" "64",
  "clojure-version-string" "1.11.1",
  "spec-version" "21"},
 "sample-variance" [3.046342253866968E-6 [0.0 0.0]],
 "outlier-variance" 0.13888888888888873,
 "final-gc-time" 76400143,
 "lower-q" [0.10018448 [0.10018448 0.10257754300000001]],
 "warmup-time" 5709716578,
 "overhead" 8.451731464212423E-9,
 "variance"
 [3.1837051655162608E-6 [3.060973957574988E-7 5.718892606608664E-6]],
 "options"
 {"progress" true,
  "target-execution-time" 100000000,
  "max-gc-attempts" 100,
  "print-result" true,
  "warmup-jit-period" 5000000000,
  "quick" true,
  "overhead" 8.451731464212423E-9,
  "bootstrap-size" 500,
  "tail-quantile" 0.025,
  "verbose" true,
  "samples" 6},
 "tail-quantile" 0.025,
 "samples"
 [100184480 103905666 104768084 102577543 104810285 103754943]}

Returns a map of results for programmatic manipulation.
For example, benchmark run 5 return this value:
20

We can reproduce the same benchmark by copy-pasting the above serialized config string into the Java program.
Evaluation count : 6 in 6 samples of 1 calls.
             Execution time mean : 103.172682 ms
    Execution time std-deviation : 1.125590 ms
   Execution time lower quantile : 101.893942 ms ( 2.5%)
   Execution time upper quantile : 104.607670 ms (97.5%)
                   Overhead used : 8.253211 ns
To reproduce via bench(Callable, String), use: "{\"quick\" true, \"max-gc-attempts\" 100, \"samples\" 6, \"target-execution-time\" 100000000, \"warmup-jit-period\" 5000000000, \"tail-quantile\" 0.025, \"bootstrap-size\" 500, \"overhead\" 8.253210752367998E-9}"
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  34.252 s
[INFO] Finished at: 2024-04-07T01:13:16-05:00
[INFO] ------------------------------------------------------------------------
```

## License

Copyright © 2024 Ambrose Bonnaire-Sergeant

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
