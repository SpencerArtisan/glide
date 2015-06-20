Planet IDE
==========

An Integrated Development Environment for kids.

The aim of this project is to give children (aged 10 and upwards) a tool to write code.
It is language agnostic, but as a first pass will only support groovy.

To build

    ./gradlew clean build dist

To run 

    ./run.sh
 
To upload jars to nexus you need to have nexusUsername and nexusPassword defined in ~/.gradle/gradle.properties, then

    ./gradlew uploadArchive

This uploads the jars to the nexus repo here

    http://leonandjosh.ddns.net:8081/nexus

To build the executables you need to download jwrapper and the JRE-1.8 to this directory here - http://www.jwrapper.com/download.html.

New release process:

1. Update the version number in build.gradle
2. ./gradlew clean build dist
3. ./package.sh
4. ./gradlew uploadArchives




Troubleshooting
---------------
DesktopLauncher not found - Means you did a gradle build without the dist

