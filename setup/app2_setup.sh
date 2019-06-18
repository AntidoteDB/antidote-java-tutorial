#!/bin/bash

docker build -t client ../bookstore
docker run -ti --rm --name app2 --hostname="app2" -v $(pwd)/../bookstore:/code --network="setup_local2" client /bin/bash
