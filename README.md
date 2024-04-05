# Running Criterium benchmarks from Java and Maven

Clojure's [criterium](https://github.com/hugoduncan/criterium/) is a great library for microbenchmarking on the JVM.
This project packages it up for use from other JVM languages.

## API

There is one public method `Criterium.bench(Runnable)`. It takes a runnable that
runs the benchmark. You should initialize your benchmark (if appropriate) before returning the Runnable.

```java
package example_benchmark;

import com.ambrosebs.criterium_from_java.Criterium;

public class Main {
  public static void main(String[] args) {
    Criterium.bench(new Runnable() {
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

```
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
```


## Example

There is an example benchmark that is run via Maven.

```
$ ./run.sh
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------------< crit-bench:crit-bench >------------------------
[INFO] Building crit-bench 0.1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- exec:3.2.0:java (default-cli) @ crit-bench ---
Warm up the benchmark here, load code etc.,
Ready to run!
Running...
x86_64 Mac OS X 14.3 8 cpu(s)
OpenJDK 64-Bit Server VM 21.0.1+12-LTS
Runtime arguments: -Dclassworlds.conf=/usr/local/Cellar/maven/3.9.6/libexec/bin/m2.conf -Dmaven.home=/usr/local/Cellar/maven/3.9.6/libexec -Dlibrary.jansi.path=/usr/local/Cellar/maven/3.9.6/libexec/lib/jansi-native -Dmaven.multiModuleProjectDirectory=/Users/ambrose/Projects/els-2024/crit-bench
Evaluation count : 16868174100 in 60 samples of 281136235 calls.
      Execution time sample mean : -4.714281 ns
             Execution time mean : -4.711263 ns
Execution time sample std-deviation : 0.165075 ns
    Execution time std-deviation : 0.167275 ns
   Execution time lower quantile : -4.810114 ns ( 2.5%)
   Execution time upper quantile : -4.538830 ns (97.5%)
                   Overhead used : 8.277237 ns

Found 2 outliers in 60 samples (3.3333 %)
        low-severe       1 (1.6667 %)
        low-mild         1 (1.6667 %)
 Variance from outliers : 22.2086 % Variance is moderately inflatedDone!
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:33 min
[INFO] Finished at: 2024-04-05T13:39:34-05:00
[INFO] ------------------------------------------------------------------------
```


## Dependencies

See pom.xml for extra dependencies needed:

```xml
  <dependencies>
    <dependency>
      <groupId>org.clojure</groupId>
      <artifactId>clojure</artifactId>
      <version>1.11.1</version>
    </dependency>
    <dependency>
      <groupId>criterium</groupId>
      <artifactId>criterium</artifactId>
      <version>0.4.6</version>
    </dependency>
  </dependencies>
```

## How to make your own benchmarks

Copy `java/crit_bench/Main.java` into your project.

Redefine `crit_bench.Main.myBenchmark()` to return a Runnable
that runs your benchmark. Now call `crit_bench.Main.entry()` to
run your benchmark, or use `crit_bench.Main.main(String[])` if you
need a main method.

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
