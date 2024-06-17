#! /bin/bash

mvn -Dmaven.test.skip=true clean package

cd ./bci-auth
docker build -t registry.cn-hangzhou.aliyuncs.com/bci2024/bci-auth:$1 .
docker login --username=TachibanaSoya registry.cn-hangzhou.aliyuncs.com --password=schoolcece02
docker push registry.cn-hangzhou.aliyuncs.com/bci2024/bci-auth:$1

cd ../bci-code
docker build -t registry.cn-hangzhou.aliyuncs.com/bci2024/bci-code:$1 .
docker login --username=TachibanaSoya registry.cn-hangzhou.aliyuncs.com --password=schoolcece02
docker push registry.cn-hangzhou.aliyuncs.com/bci2024/bci-code:$1

cd ../bci-task
docker build -t registry.cn-hangzhou.aliyuncs.com/bci2024/bci-task:$1 .
docker login --username=TachibanaSoya registry.cn-hangzhou.aliyuncs.com --password=schoolcece02
docker push registry.cn-hangzhou.aliyuncs.com/bci2024/bci-task:$1

cd ../bci-competition
docker build -t registry.cn-hangzhou.aliyuncs.com/bci2024/bci-competition:$1 .
docker login --username=TachibanaSoya registry.cn-hangzhou.aliyuncs.com --password=schoolcece02
docker push registry.cn-hangzhou.aliyuncs.com/bci2024/bci-competition:$1

cd ../bci-gateway
docker build -t registry.cn-hangzhou.aliyuncs.com/bci2024/bci-gateway:$1 .
docker login --username=TachibanaSoya registry.cn-hangzhou.aliyuncs.com --password=schoolcece02
docker push registry.cn-hangzhou.aliyuncs.com/bci2024/bci-gateway:$1
