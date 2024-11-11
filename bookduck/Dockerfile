FROM openjdk:21-slim

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

COPY ./bookduck/src/main/resources /app/src/main/resources

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]
