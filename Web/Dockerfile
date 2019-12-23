# Build stage

FROM maven:3.6.1-jdk-8 as maven

WORKDIR /app

COPY . .

RUN mvn package

CMD ["bash"]

# Final image with jar
FROM openjdk:8-alpine

WORKDIR /app

COPY --from=maven /app/target/web-portal-*.jar ./

EXPOSE 8080

CMD ["java", "-jar", "/app/target/web-portal-0.1.0.jar"]