# Build stage

FROM maven:3.6.1-jdk-8 as maven

WORKDIR /Web

COPY . .

RUN mvn clean package

CMD ["bash"]

# Final image with jar
FROM openjdk:8-alpine

WORKDIR /Web

RUN chmod +x /Web/target/web-portal-0.1.0-SNAPSHOT.jar

COPY --from=maven /Web/target/web-portal-*.jar ./web-portal-0.1.0.jar

EXPOSE 8080
CMD ["java", "-jar", "/Web/target/web-portal-0.1.0-SNAPSHOT.jar"]