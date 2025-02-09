FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
# Copy the secret file into resources directory
RUN ls -la /etc/secrets
RUN ls -la /home/
RUN ls -la /home/gradle/
RUN ls -la /home/gradle/src/
RUN ls -la /home/gradle/src/server/
RUN ls -la /home/gradle/src/server/src/
COPY --chown=gradle:gradle /etc/secrets/web-client.json /home/gradle/src/server/src/main/resources/web-client.json
WORKDIR /home/gradle/src
RUN gradle server:buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/server-all.jar
ENTRYPOINT ["java","-jar", "/app/server-all.jar"]