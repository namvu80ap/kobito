FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD kobito-ai-0.0.1-SNAPSHOT.jar kobito-ai.jar
CMD java -jar  -Dspring.profiles.active=production kobito-ai.jar