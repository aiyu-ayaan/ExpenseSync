FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src

ARG WEB_CLIENT_JSON
RUN echo "$WEB_CLIENT_JSON" > /home/gradle/src/server/src/main/resources/web-client.json
RUN ls -la /home/gradle/src/server/src/main/resources

WORKDIR /home/gradle/src/server/src
RUN gradle server:buildFatJar

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/server/build/libs/*.jar /app/server-all.jar
ENTRYPOINT ["java", "-jar", "/app/server-all.jar"]
