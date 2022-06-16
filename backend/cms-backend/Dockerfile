FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cms-backend.jar
ENTRYPOINT ["java", "-jar", "/cms-backend.jar"]