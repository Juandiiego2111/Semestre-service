FROM openjdk:21
LABEL authors="juandiiego2111"
WORKDIR /app
COPY target/semestreservice-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-jar", "semestreservice-0.0.1-SNAPSHOT.jar"]