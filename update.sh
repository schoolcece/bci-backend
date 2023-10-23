#! /bin/bash

mvn -Dmaven.test.skip=true -gs ./maven-settiing.xml clean package

cd ./$1
docker build -t registry.cn-beijing.aliyuncs.com/bci2023/$1:$2 .
docker login --username=aliyun8925885825 registry.cn-beijing.aliyuncs.com --password=hcc15735181737
docker push registry.cn-beijing.aliyuncs.com/bci2023/$1:$2
