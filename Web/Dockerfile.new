FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/web-portal-0.1.0-SNAPSHOT.jar web-portal.jar
ENTRYPOINT exec java -jar web-portal.jar