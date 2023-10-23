#! /bin/bash

mvn -Dmaven.test.skip=true -gs ./maven-settiing.xml clean package

cd ./bci-code
docker build -t bci-code:$1 .
docker run --name bci-code -d -v /nfs/code:/nfs/code bci-code:$1

cd ../bci-task
docker build -t bci-task:$1 .
docker run -d --name bci-task -v /nfs/code:/nfs/code bci-task:$1

cd ../bci-log
docker build -t bci-log:$1 .
docker run --name bci-log -d  bci-log:$1

cd ../bci-auth-server
docker build -t bci-auth-server:$1 .
docker run --name bci-auth-server -d  bci-auth-server:$1

cd ../bci-scheduler
docker build -t bci-scheduler:$1 .
docker run --name bci-scheduler -d  bci-scheduler:$1

cd ../bci-gateway
docker build -t bci-gateway:$1 .
docker run --name bci-gateway -d  -p 88:88 bci-gateway:$1