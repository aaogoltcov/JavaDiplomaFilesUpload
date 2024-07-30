FROM arm64v8/gradle:8.6.0-jdk as builder
COPY --chown=gradle:gradle . /source/gradle/src
WORKDIR /source/gradle/src
RUN gradle clean build -x test

FROM arm64v8/openjdk:17-jdk
WORKDIR /app
COPY --from=builder /source/gradle/src/build/libs/JavaDiplomaFilesUpload-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]