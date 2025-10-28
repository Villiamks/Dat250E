FROM gradle:9.0.0-jdk21

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=0 /app/build/libs/*.jar app.jar

# Create and switch to non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]