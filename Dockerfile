FROM adoptopenjdk:11-jre-hotspot
WORKDIR src
ADD impl/target/microservice-post-impl-1.0.0-SNAPSHOT.jar microservice-post-impl.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "microservice-post-impl.jar"]