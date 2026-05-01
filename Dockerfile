FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/scm2.0-0.0.1-SNAPSHOT.jar /app/scm2.0.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "scm2.0.jar"]

