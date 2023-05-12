FROM openjdk:11.0.13-jre-slim

ADD ./build/libs/tenpo-challenge-application-0.0.1-SNAPSHOT.jar tenpo-challenge-application.jar
ENTRYPOINT ["java", "-jar" , "/tenpo-challenge-application.jar"]