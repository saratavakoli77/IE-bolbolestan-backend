FROM maven:3.6.3-jdk-11-slim AS builder

COPY pom.xml /build/
COPY src /build/src/

RUN mvn -f /build/pom.xml clean package

FROM openjdk:11-jre-slim

COPY --from=builder /build/target/Bolbolestan-0.0.1-SNAPSHOT.jar /app/bolbolestan.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/bolbolestan.jar"]
