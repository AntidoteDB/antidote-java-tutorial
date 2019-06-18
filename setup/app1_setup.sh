#!/bin/bash

docker build -t client ../bookstore
docker run -ti --rm --name app1 --hostname="app1" -v $(pwd)/../bookstore:/code --network="setup_local1" client /bin/bash
