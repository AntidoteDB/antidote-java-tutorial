# AntidoteDB Java Tutorial

This is a sample application to demonstrate how to build applications that use [AntidoteDB][AntidoteDB-website] as backend database.

This tutorial is made for [Antidote Bookstore Tutorial](https://github.com/AntidoteDB/antidote-java-tutorial)

## What you need
* [Java 1.7 or Higher](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* An IDE for java development
* [Gradle](https://gradle.org/) build tool
* [Docker](https://docs.docker.com/engine/installation/)
* [docker-compose](https://docs.docker.com/compose/install/)

## Getting started
This repository is divided into two source directories:
* `setup`: scripts and docker files for running two instances of Antidote and two instances of the Java application
* `bookstore`: source of the tutorial application

| Note: make sure you have the software requirements listed above before following next steps. |
| --- |

### Step 1: Build Bookstore application
First we need to build the source code of our Bookstore application, we will use Gradle as follows:
```bash
cd bookstore
./gradlew build
```

Note that you will need to rebuild the sources each time you make a change.

More information about Gradle [Here](https://docs.gradle.org/current/userguide/userguide.html)

### Step 2 : Starting antidote nodes
The following script starts two antidote docker containers and set up the inter-dc replication.
```bash
# in setup/
./start_antidote.sh
```

### Step 3 : Starting the application
Open two shells:
* In the first one, start the first app:
```bash
# in setup/
./app1.sh
# then connect the app to Antidote instance 1:
bookstore@antidote1> connect antidote1 8087
```
* We will do the same for the second app:
```bash
# in setup/
./app2.sh
# then connect the app to Antidote instance 2:
bookstore@antidote2> connect antidote2 8087
```

Now we built this configuration:

![Tutorial Figure](./doc/tutorial-figure.png "Tutorial figure")

### Step 4 : Try the following app commands
Some commands are already implemented in the app, lets try them:
~~~~
inc testbucket mycounter
getcounter testbucket mycounter
additem testbucket myset newitem
getset testbucket myset
~~~~

To stop the app:
~~~~
quit
~~~~

You can also try to disconnect Antidote servers to simulate network partitions, commit some concurrent updates, then reconnect to merge CRDTs:
~~~~bash
# in setup/
./disconnect.sh #to disrupt communication between Antidote1 and Antidote2 nodes

#then update objects while disconnected

./connect.sh #connect Antidote nodes back
~~~~

To stop Antidote Nodes:
```bash
# in setup/
./stop_antidote.sh
```

### Step 5 : Hands On!!!
We now want to build our Bookstore app. The provided sources are divided into 3 files (located in bookstore/src/main/):
* `BookStore.java`: this file contains the command interface and the starting point of the app.
* `DemoCommandsExecutor.java`: this file contains the implementation of the demo commands we have seen in the previous Step. You can use them as examples to implement your own commands.
* `BookCommands.java`: Here is the file where you need to implement Bookstore commands, fill in the methods to add necessary.

### Resources:
Some useful references:
* [Antidote API Javadoc](https://www.javadoc.io/doc/eu.antidotedb/antidote-java-client/0.3.1)
* [AntidoteDB source code](https://github.com/AntidoteDB/antidote)
* [AntidoteDB Documentation](https://antidotedb.gitbook.io/documentation/)
* [Antidote Docker reference](https://github.com/AntidoteDB/docker-antidote/blob/master/README.md)

## License
View [license information](https://github.com/AntidoteDB/antidote/blob/master/LICENSE) for the software contained in this image.

[AntidoteDB-website]: https://www.antidotedb.eu/
