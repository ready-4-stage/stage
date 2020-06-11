FROM openjdk:11-jre-slim

ENV PROFILE dev
ENV DB_USER ''
ENV DB_PWD ''

RUN mkdir /app
COPY stage-server/target/app.jar /app/app.jar

ENTRYPOINT java -Dspring.profiles.active=$PROFILE -Dstage.db.user=$DB_USER -Dstage.db.pwd=$DB_PWD -jar /app/app.jar
