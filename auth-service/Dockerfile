FROM openjdk:11
VOLUME /tmp
EXPOSE 9100
ADD ./target/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "/auth-service.jar"]