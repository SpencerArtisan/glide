[![Build Status](https://travis-ci.org/SpencerArtisan/glide.svg?branch=master)](https://travis-ci.org/SpencerArtisan/glide)

![GLIDE](./installers/glide-logo.png)
The General Language Integrated Development Environment...for kids
==================================================================

The aim of this project is to give children (aged 10 and upwards) a tool to write code.
It is language agnostic and currently supports Groovy, Ruby and Javascript.

Setting up a Development Environment
------------------------------------

To upload jars to [nexus](http://leonandjosh.ddns.net:8081/nexus) you need to have nexusUsername and nexusPassword defined in ~/.gradle/gradle.properties.

To build the executables you need to download jwrapper and the JRE-1.8 to the root directory from [here] (http://www.jwrapper.com/download.html).

The sister runtime project can be found [here](https://github.com/phil-anderson/blurp).  This project manages the program execution environment.  It has a separate versioning strategy.  

Release Process
---------------

1. Update the version numbers in ``build.gradle``, ``installers/jwrapper-glide.xml`` and ``run.sh``
2. Create release branch and push to github
3. Run jenkins build on release branch - http://leonandjosh.ddns.net:8080/job/glide/
4. Log into [sonatype](https://oss.sonatype.org/#stagingRepositories) and find the staging repository
5. Confirm it contains core and desktop artifacts, together with installers
6. Close the staging repository and check it passed all validation rules
7. Release the staging repository
8. Confirm it is now in the [sonatype release repository](https://oss.sonatype.org/#view-repositories;releases~browsestorage)


Troubleshooting
---------------
DesktopLauncher not found - Means you did a gradle build without the dist

