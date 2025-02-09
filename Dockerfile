FROM gradle:8.10.2-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src

ARG WEB_CLIENT_JSON
RUN echo "$WEB_CLIENT_JSON" > /home/gradle/src/server/src/main/resources/web-client.json
RUN ls -la /home/gradle/src/server/src/main/resources
RUN echo /home/gradle/src/server/src/main/resources/web-client.json

WORKDIR /home/gradle/src
RUN gradle buildFatJar --stacktrace --info --no-daemon

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/server/build/libs/*.jar /app/server-all.jar
ENTRYPOINT ["java", "-jar", "/app/server-all.jar"]
