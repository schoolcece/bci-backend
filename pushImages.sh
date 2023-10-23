#! /bin/bash

mvn -Dmaven.test.skip=true -gs ./maven-settiing.xml clean package

cd ./bci-code
docker build -t registry.cn-beijing.aliyuncs.com/bci2023/bci-code:$1 .
docker login --username=aliyun8925885825 registry.cn-beijing.aliyuncs.com --password=hcc15735181737
docker push registry.cn-beijing.aliyuncs.com/bci2023/bci-code:$1

cd ../bci-task
docker build -t registry.cn-beijing.aliyuncs.com/bci2023/bci-task:$1 .
docker login --username=aliyun8925885825 registry.cn-beijing.aliyuncs.com --password=hcc15735181737
docker push registry.cn-beijing.aliyuncs.com/bci2023/bci-task:$1

cd ../bci-log
docker build -t registry.cn-beijing.aliyuncs.com/bci2023/bci-log:$1 .
docker login --username=aliyun8925885825 registry.cn-beijing.aliyuncs.com --password=hcc15735181737
docker push registry.cn-beijing.aliyuncs.com/bci2023/bci-log:$1

cd ../bci-auth-server
docker build -t registry.cn-beijing.aliyuncs.com/bci2023/bci-auth-server:$1 .
docker login --username=aliyun8925885825 registry.cn-beijing.aliyuncs.com --password=hcc15735181737
docker push registry.cn-beijing.aliyuncs.com/bci2023/bci-auth-server:$1

cd ../bci-scheduler
docker build -t registry.cn-beijing.aliyuncs.com/bci2023/bci-scheduler:$1 .
docker login --username=aliyun8925885825 registry.cn-beijing.aliyuncs.com --password=hcc15735181737
docker push registry.cn-beijing.aliyuncs.com/bci2023/bci-scheduler:$1

cd ../bci-gateway
docker build -t registry.cn-beijing.aliyuncs.com/bci2023/bci-gateway:$1 .
docker login --username=aliyun8925885825 registry.cn-beijing.aliyuncs.com --password=hcc15735181737
docker push registry.cn-beijing.aliyuncs.com/bci2023/bci-gateway:$1
