
This is a sample application to demonstrate how to build applications that use Antidote as backend database.

* setup - scripts and docker files for running two instances of Antidote and two instances of the jave application
* bookstore - source of the application

### What you need
* An IDE for java development
* [Docker](https://docs.docker.com/engine/installation/)
* [docker-compose](https://docs.docker.com/compose/install/)


### Build Bookstore application
In bookstore/
* ./gradlew build

### Starting antidote nodes
In setup/
* ./start_antidote.sh
* ./stop_antidote.sh

Scripts starts(or stop) two antidote docker containers and set up the inter-dc replication.

### Starting the application
* app1.sh - starts application instance 1 which can connect to antidote instance 1
* app2.sh - starts application instance 2 which can connect to antidote instance 2
