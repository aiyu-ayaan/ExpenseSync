FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src

# Create the resources directory if not exists
RUN mkdir -p /home/gradle/src/server/src/main/resources

# Write the secret from an environment variable
RUN echo "$WEB_CLIENT_JSON"
ARG WEB_CLIENT_JSON
RUN echo "$WEB_CLIENT_JSON" > /home/gradle/src/server/src/main/resources/web-client.json

WORKDIR /home/gradle/src
RUN gradle buildFatJar

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/server-all.jar
ENTRYPOINT ["java", "-jar", "/app/server-all.jar"]
