#FROM gradle:7.3.2-jdk11-alpine AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY gradle /workspace/gradle
#COPY gradlew.bat /workspace
#COPY build.gradle /workspace
#COPY settings.gradle /workspace
#COPY gradlew /workspace
#COPY src /workspace/src
#RUN gradle

FROM openjdk:17-alpine
COPY build/libs/CommentService-0.0.1-SNAPSHOT.jar server.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "server.jar"]