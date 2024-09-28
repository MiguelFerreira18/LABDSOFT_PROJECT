FROM openjdk:17

COPY target/SmartCityAPI.jar /app/SmartCityAPI.jar


WORKDIR /app

EXPOSE 9092

ENTRYPOINT ["java","-jar","SmartCityAPI.jar"]
