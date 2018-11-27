FROM openjdk:8-jre-slim

RUN mkdir /app

WORKDIR /app

ADD ./api/target/institution-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8081

CMD java -jar institution-api-1.0.0-SNAPSHOT.jar