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

There is an example benchmark that is run via Maven.

```
$ ./run.sh
+ mvn clean
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------< com.ambrosebs:criterium-from-java >------------------
[INFO] Building criterium-from-java 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- clean:3.2.0:clean (default-clean) @ criterium-from-java ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.322 s
[INFO] Finished at: 2024-04-06T10:18:53-05:00
[INFO] ------------------------------------------------------------------------
+ mvn compile
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------< com.ambrosebs:criterium-from-java >------------------
[INFO] Building criterium-from-java 1.0.0-SNAPSHOT
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
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.289 s
[INFO] Finished at: 2024-04-06T10:18:56-05:00
[INFO] ------------------------------------------------------------------------
+ mvn exec:java -Dexec.mainClass=com.ambrosebs.criterium_from_java.Criterium
[INFO] Scanning for projects...
[INFO]
[INFO] -----------------< com.ambrosebs:criterium-from-java >------------------
[INFO] Building criterium-from-java 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- exec:3.2.0:java (default-cli) @ criterium-from-java ---
Running example benchmark
Setting up the benchmark
Ready to run!
Running...
Estimating sampling overhead
Warming up for JIT optimisations 10000000000 ...
  compilation occurred before 67 iterations
  compilation occurred before 419928 iterations
  compilation occurred before 839789 iterations
  compilation occurred before 2099372 iterations
  compilation occurred before 2519233 iterations
  compilation occurred before 2939094 iterations
  compilation occurred before 7137704 iterations
  compilation occurred before 10916453 iterations
  compilation occurred before 16374646 iterations
  compilation occurred before 22252700 iterations
  compilation occurred before 23932144 iterations
  compilation occurred before 25611588 iterations
  compilation occurred before 31489642 iterations
  compilation occurred before 39047140 iterations
  compilation occurred before 47864221 iterations
  compilation occurred before 52062831 iterations
  compilation occurred before 52902553 iterations
  compilation occurred before 54162136 iterations
  compilation occurred before 74315464 iterations
  compilation occurred before 75155186 iterations
  compilation occurred before 79353796 iterations
  compilation occurred before 79773657 iterations
  compilation occurred before 106224900 iterations
  compilation occurred before 106644761 iterations
  compilation occurred before 107064622 iterations
  compilation occurred before 131836421 iterations
  compilation occurred before 133096004 iterations
  compilation occurred before 135195309 iterations
  compilation occurred before 152829471 iterations
  compilation occurred before 153249332 iterations
  compilation occurred before 153669193 iterations
  compilation occurred before 158287664 iterations
  compilation occurred before 160806830 iterations
  compilation occurred before 169204050 iterations
  compilation occurred before 176341687 iterations
  compilation occurred before 214549038 iterations
  compilation occurred before 237641393 iterations
  compilation occurred before 276688466 iterations
  compilation occurred before 277108327 iterations
  compilation occurred before 277528188 iterations
  compilation occurred before 321193732 iterations
  compilation occurred before 400967322 iterations
  compilation occurred before 414822735 iterations
  compilation occurred before 421960372 iterations
  compilation occurred before 423219955 iterations
  compilation occurred before 485359383 iterations
  compilation occurred before 520627707 iterations
  compilation occurred before 563033668 iterations
  compilation occurred before 702007659 iterations
  compilation occurred before 938389402 iterations
  compilation occurred before 946366761 iterations
  compilation occurred before 971978282 iterations
  compilation occurred before 988352861 iterations
  compilation occurred before 1022781463 iterations
  compilation occurred before 1053851177 iterations
  compilation occurred before 1055950482 iterations
  compilation occurred before 1095837277 iterations
  compilation occurred before 1097516721 iterations
  compilation occurred before 1126906991 iterations
  compilation occurred before 1127326852 iterations
  compilation occurred before 1127746713 iterations
  compilation occurred before 1138663099 iterations
  compilation occurred before 1181069060 iterations
Estimating execution count ...
Sampling ...
Final GC...
Checking GC...
Finding outliers ...
Bootstrapping ...
Checking outlier significance
Warming up for JIT optimisations 10000000000 ...
  compilation occurred before 30 iterations
  compilation occurred before 178769 iterations
  compilation occurred before 357508 iterations
  compilation occurred before 1251203 iterations
  compilation occurred before 1608681 iterations
  compilation occurred before 1787420 iterations
  compilation occurred before 3038593 iterations
  compilation occurred before 3574810 iterations
  compilation occurred before 10903109 iterations
  compilation occurred before 25023490 iterations
  compilation occurred before 28598270 iterations
  compilation occurred before 62379941 iterations
  compilation occurred before 63094897 iterations
  compilation occurred before 81683753 iterations
  compilation occurred before 81862492 iterations
  compilation occurred before 82041231 iterations
  compilation occurred before 173019382 iterations
  compilation occurred before 173198121 iterations
  compilation occurred before 173376860 iterations
  compilation occurred before 416283161 iterations
  compilation occurred before 421109114 iterations
  compilation occurred before 423790199 iterations
  compilation occurred before 448456181 iterations
  compilation occurred before 448813659 iterations
  compilation occurred before 449886093 iterations
  compilation occurred before 472407207 iterations
  compilation occurred before 475981987 iterations
  compilation occurred before 476160726 iterations
  compilation occurred before 489923629 iterations
  compilation occurred before 499039318 iterations
  compilation occurred before 1051164089 iterations
Estimating execution count ...
Sampling ...
Final GC...
Checking GC...
Finding outliers ...
Bootstrapping ...
Checking outlier significance
x86_64 Mac OS X 14.3 8 cpu(s)
OpenJDK 64-Bit Server VM 21.0.1+12-LTS
Runtime arguments: -Dclassworlds.conf=/usr/local/Cellar/maven/3.9.6/libexec/bin/m2.conf -Dmaven.home=/usr/local/Cellar/maven/3.9.6/libexec -Dlibrary.jansi.path=/usr/local/Cellar/maven/3.9.6/libexec/lib/jansi-native -Dmaven.multiModuleProjectDirectory=/Users/ambrose/Projects/criterium-from-java
Evaluation count : 17667680220 in 60 samples of 294461337 calls.
Done!
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:32 min
[INFO] Finished at: 2024-04-06T10:20:30-05:00
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
