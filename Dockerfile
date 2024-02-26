FROM openjdk:17-jdk-slim

WORKDIR /rest-app

CMD ["./gradlew", "clean", "bootJar"]

COPY build/libs/*.jar build/libs/rest-app-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","/app.jar"]