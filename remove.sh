#! /bin/bash

docker rm -f bci-code
docker rmi bci-code:$1

docker rm -f bci-task
docker rmi bci-task:$1

docker rm -f bci-log
docker rmi  bci-log:$1

docker rm -f bci-auth-server
docker rmi  bci-auth-server:$1

docker rm -f bci-scheduler
docker rmi  bci-scheduler:$1

docker rm -f bci-gateway
docker rmi bci-gateway:$1