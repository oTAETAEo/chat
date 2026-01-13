FROM eclipse-temurin:21-jre
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV PROFILES=default
ENV ENV=local
ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=${PROFILES} -Dserver.env=${ENV} -jar /app.jar"]