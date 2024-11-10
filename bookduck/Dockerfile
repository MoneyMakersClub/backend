FROM openjdk:21-slim

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 로컬 파일을 컨테이너에 복사
COPY ./bookduck/src/main/resources/mmc-bookduck-ocr.json /home/ubuntu/mmc-bookduck-ocr.json

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]
