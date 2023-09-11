FROM openjdk:11
VOLUME /tmp
ADD ./target/users-0.0.1-SNAPSHOT.jar users-service.jar
ENTRYPOINT ["java", "-jar", "/users-service.jar"]