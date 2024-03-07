FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -Dskiptests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/pdf-generation-service-0.0.1-SNAPSHOT.jar  pdf-generation-service.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "pdf-generation-service.jar"]
