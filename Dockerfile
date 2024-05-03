FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/RestApiApplication-0.0.1-SNAPSHOT.jar .
COPY docker-startup.sh .
RUN chmod +x docker-startup.sh
EXPOSE 8087
CMD ["./docker-startup.sh"]