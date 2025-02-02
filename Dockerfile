FROM eclipse-temurin:17-jdk

ARG JAR_FILE=target/display-service-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} display-service.jar

ENTRYPOINT ["java", "-jar", "/display-service.jar"]