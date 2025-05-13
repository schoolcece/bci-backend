FROM openjdk:17
EXPOSE 8080

VOLUME /tmp
ADD target/*.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","-Xms4096m","/app.jar","--spring.profiles.active=prod"]
