#!/bin/bash

docker build -t client ../bookstore
docker create -t --rm --name tutorial --hostname="tutorial" -e "ANTIDOTE_TUTORIAL_SAVE=0" -v $(pwd)/../bookstore:/code client
docker network connect setup_local1 tutorial
docker network connect setup_local2 tutorial
docker start tutorial
docker exec -ti tutorial /bin/bash
docker stop tutorial

