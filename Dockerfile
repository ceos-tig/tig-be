FROM --platform=linux/amd64 eclipse-temurin:17-jdk
VOLUME /tmp
COPY server/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]