FROM eclipse-temurin:19 as build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN --mount=type=cache,target=/root/.m2 ./mvnw package --batch-mode
ARG JAR_FILE=target/*.jar
RUN cp ${JAR_FILE} target/app.jar
ENTRYPOINT ["java", "-jar", "/workspace/app/target/app.jar"]