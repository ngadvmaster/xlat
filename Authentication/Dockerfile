FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/authentication-0.0.1-SNAPSHOT.jar egp-authentication.jar
ENTRYPOINT exec java -jar egp-authentication.jar