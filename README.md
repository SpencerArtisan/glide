Planet IDE
==========

An Integrated Development Environment for kids.

The aim of this project is to give children (aged 10 and upwards) a tool to write code.
It is language agnostic, but as a first pass will only support groovy.

To build

    ./gradlew clean build dist

To run 

    ./run.sh
 

To package up a linux version of the app

    Download the linux jdk from oracle and zip into jdk.zip in the root folder of this project
    java -jar packr.jar packr-linux.json

Troubleshooting
---------------
DesktopLauncher not found - Means you did a gradle build without the dist

