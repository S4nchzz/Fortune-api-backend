FROM openjdk:22
LABEL authors="Iyan Sanchez da Costa"
COPY ./target/fortune-api-0.0.1-SNAPSHOT.jar ./app/app.jar

ENTRYPOINT ["java", "-jar", "./app/app.jar"]