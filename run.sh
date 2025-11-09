#!/bin/bash
mvn clean package
java \
 --add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
 --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED \
 -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC \
 -jar target/nano-trader-1.0-SNAPSHOT-jar-with-dependencies.jar

