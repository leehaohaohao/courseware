FROM openjdk:17-jdk-slim
WORKDIR /app
COPY courseware-0.0.1.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 10001 10002