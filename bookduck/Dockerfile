FROM openjdk:21-slim

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV CLOUD_GCP_STORAGE_CREDENTIALS_LOCATION=/app/src/main/resources/mmc-bookduck-ocr.json

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]
