FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/06-springboot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar