# AntidoteDB Java Tutorial

This is a sample application to demonstrate how to build applications that use [AntidoteDB][AntidoteDB-website] as backend database.

This tutorial is made for [Antidote Bookstore Tutorial](https://github.com/AntidoteDB/antidote-java-tutorial)

## What you need
* [Docker](https://docs.docker.com/engine/installation/)
* [docker-compose](https://docs.docker.com/compose/install/)
* An IDE for java development

## Clone source code
git clone https://github.com/AntidoteDB/antidote-java-tutorial

## Getting started
This repository is divided into two source directories:
* `setup`: scripts and docker files for running two instances of Antidote and two instances of the Java application
* `bookstore`: source of the tutorial application

| Note: make sure you have the software requirements listed above before following next steps. |
| --- |

### Step 1: Antidote-kernel Jupyter Notebook
The [Jupyter](https://jupyter.org) Antidote kernel is a Jupyter kernel based on the [Jupyter Groovy Kernel](https://github.com/lappsgrid-incubator/jupyter-groovy-kernel) that connects to Antidote services. It allows users to interact with Antidote databases and observe how Antidote resolves inconsistencies in case of network failure.

To start up the Antidote-kernel notebook:
```bash
# in setup/
$ docker-compose up
```

### Step 2 : Interactive Tutorial / Game
The interactive tutorial is an executable that presents a series of tasks/challenges. Each task has an outcome, SUCCESS or FAIL.
Solving a task allows you to try out the next task.
To try the interactive tutorial:

First we need to start two Antidote docker containers and set up the inter-dc replication.

You can either keep the Jupyter Notebook from Step 1 active as it has already deployed this setup.

Alternatively, to start the Antidote containers in the background use the following script (let's label this shell as _setup shell_ for later reference):
```bash
# in setup/
$ ./start_antidote.sh
```

To start the tutorial, open a new shell (we label this shell _tutorial shell_):
```bash
# in setup/
$ ./tutorial_setup.sh
# build the tutorial
root@tutorial$ ./gradlew build
# run the tutorial executable
root@tutorial$ ./tutorial.sh
```

In case you want to reset your progress, in the _setup shell_ do:
```bash_
$ ./stop_antidote.sh
$ ./start_antidote.sh
```

Here are some bits of information that will be useful in various parts of the interactive tutorial:

#### Starting the Bookstore application
Open two new shells:
* In the first one, start the first app (we label this shell _app1 shell_):
```bash
# in setup/
$ ./app1_setup.sh
# build the app code
root@app1$ ./gradlew build
# and start the app
root@app1$ ./app1.sh
# then connect the app to Antidote instance 1:
bookstore@antidote1 > connect antidote1 8087
```

* Do the same for the second app (we label this shell _app2 shell_):
```bash
# in setup/
$ ./app2_setup.sh
# build the app code
root@app2$ ./gradlew build
# and start the app
root@app2$ ./app2.sh
# then connect the app to Antidote instance 2:
bookstore@antidote2 > connect antidote2 8087
```

We have now deployed this configuration:

![Tutorial Figure](./doc/tutorial-figure.png "Tutorial figure")


#### Creating a network partition
To disconnect the two Antidote servers in order to simulate a network partition, in _setup shell_:
```bash
# in setup/
./disconnect.sh #to disrupt communication between Antidote1 and Antidote2 nodes

#then update objects while disconnected

./connect.sh #connect Antidote nodes back
```

#### Hands on: Building the Bookstore application
The provided sources are divided into 3 files (located in bookstore/src/main/java/):
* `BookStore.java`: this file contains the command interface and the starting point of the app.
* `DemoCommandsExecutor.java`: this file contains the implementation of the demo commands we have seen in the previous Step. You can use them as examples to implement your own commands.
* `BookCommands.java`: Here is the file where you need to implement Bookstore commands, fill in the methods to add necessary.

The main method is located at bookstore/src/main/java/BookStore.java.

To re-build the app after modifying the source code, in the _tutorial shell_:
```bash
root@tutorial$ ./gradlew build
```

### Resources:
Some useful references:
* [Antidote API Javadoc](https://www.javadoc.io/doc/eu.antidotedb/antidote-java-client/0.3.1)
* [AntidoteDB source code](https://github.com/AntidoteDB/antidote)
* [AntidoteDB Documentation](https://antidotedb.gitbook.io/documentation/)
* [Antidote Docker reference](https://github.com/AntidoteDB/docker-antidote/blob/master/README.md)

## License
View [license information](https://github.com/AntidoteDB/antidote/blob/master/LICENSE) for the software contained in this image.

[AntidoteDB-website]: https://www.antidotedb.eu/
