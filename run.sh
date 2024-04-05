#!/bin/sh

set -e

mvn clean
mvn compile
mvn exec:java -Dexec.mainClass="crit_bench.Main"
