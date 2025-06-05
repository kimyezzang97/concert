# Step 1: Java 17 이미지 기반
FROM eclipse-temurin:17-jdk-alpine

# Step 2: JAR 파일을 컨테이너로 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Step 3: 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]