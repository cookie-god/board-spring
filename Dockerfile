# 1단계: Gradle 빌드 환경
FROM gradle:8.3-jdk17 AS build

# Gradle 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 복사
COPY . .

# Gradle 빌드 (bootJar)
RUN ./gradlew bootJar

# 2단계: 실행 환경
FROM openjdk:17-slim

# 컨테이너 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]