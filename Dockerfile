# 1단계: Gradle 빌드 환경
FROM gradle:8.6-jdk21 AS build

# 빌드 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 복사
COPY . .

# 디렉토리 내용 확인
RUN sed -i 's/\r$//' gradlew || true

# Gradle Wrapper 실행 권한 추가 (필요 시)
RUN chmod +x gradlew

# Spring Boot JAR 빌드 (bootJar)
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행 환경
FROM openjdk:21-jdk-slim

# 애플리케이션 실행 디렉토리
WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /app/build/libs/*.jar app.jar

# JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
