#!/bin/sh

set -e

mvn clean
mvn compile
mvn exec:java -Dexec.mainClass="example_benchmark.Main"
