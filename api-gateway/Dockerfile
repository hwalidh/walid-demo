FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd -m appuser && chown appuser:appuser /app

COPY --from=build /app/target/*.jar app.jar

RUN chown appuser:appuser /app/app.jar

USER appuser

EXPOSE 8085

ENTRYPOINT ["java","-jar","app.jar"]