#!/bin/sh

set -ex

mvn clean
mvn compile
mvn exec:java -Dexec.mainClass="com.ambrosebs.criterium_from_java.Criterium"
