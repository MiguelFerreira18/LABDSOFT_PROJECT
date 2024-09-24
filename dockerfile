FROM openjdk:17

COPY target/labdsoft-0.0.1-SNAPSHOT.jar labdsoft.jar

EXPOSE 9092

ENTRYPOINT ["java","-jar","labdsoft.jar"]
