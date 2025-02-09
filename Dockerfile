FROM gradle:7-jdk11 AS build
COPY  /etc/secrets/web-client.json /home/gradle/src/server/src/main/resources/web-client.json
COPY --chown=gradle:gradle . /home/gradle/src
# Copy the secret file into resources directory
WORKDIR /home/gradle/src
RUN gradle server:buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/server-all.jar
ENTRYPOINT ["java","-jar", "/app/server-all.jar"]