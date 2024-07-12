FROM --platform=linux/amd64 openjdk:17
VOLUME /tmp
COPY server/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]