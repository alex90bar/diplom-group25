FROM openjdk:14-jdk-alpine
MAINTAINER alex90bar Baranov Alexander
COPY impl/target/microservice-post-impl-1.0.0-SNAPSHOT.jar microservice-post-impl.jar
ENTRYPOINT ["java", "-jar", "microservice-post-impl.jar"]