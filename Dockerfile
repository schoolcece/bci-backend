FROM maven:3.8.1-jdk-8-slim as builder

WORKDIR /app

COPY bci-auth-server/target/*.jar .
COPY bci-code/target/bci-code-0.0.1-SNAPSHOT.jar .
COPY bci-gateway/target/bci-gateway-0.0.1-SNAPSHOT.jar .
COPY bci-log/target/bci-log-0.0.1-SNAPSHOT.jar .
COPY bci-scheduler/target/bci-scheduler-0.0.1-SNAPSHOT.jar .
COPY bci-task/target/bci-task-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java","-jar","/app/bci-auth-server-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]