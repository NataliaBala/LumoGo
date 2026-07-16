FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /app/target/lumogo-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
