FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD kobito-integration-0.0.1-SNAPSHOT.jar kobito-integration.jar
CMD java -jar -Dspring.profiles.active=production kobito-integration.jar