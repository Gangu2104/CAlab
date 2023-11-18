#!/bin/bash
# rm -r bin
# rm -r jars

ant
ant make-jar

java -jar jars/simulator.jar src/configuration/config.xml evenorodd.txt test_cases/evenorodd.out
java -jar jars/simulator.jar src/configuration/config.xml prime.txt test_cases/prime.out
java -jar jars/simulator.jar src/configuration/config.xml fibonacci.txt test_cases/fibonacci.out
java -jar jars/simulator.jar src/configuration/config.xml palindrome.txt test_cases/palindrome.out
java -jar jars/simulator.jar src/configuration/config.xml descending.txt test_cases/descending.out
