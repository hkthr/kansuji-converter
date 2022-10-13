# syntax=docker/dockerfile:1
FROM gradle:7.5.1-jdk17-alpine as build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

FROM eclipse-temurin:17-jdk-alpine

ARG kansuji_runtime=kansujiConv-0.1.0.jar
ENV ANLTR_RUNTIME=antlr4-runtime-4.7.2.jar
ENV KANSUJI_RUNTIME=$kansuji_runtime
ENV KANSUJI_FQNAME=com.github.hkthr.KansujiConverter

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/$KANSUJI_RUNTIME /app
COPY --from=build /opt/gradle/lib/$ANLTR_RUNTIME /app

ENTRYPOINT java -cp "/app/${ANLTR_RUNTIME}:/app/${KANSUJI_RUNTIME}" ${KANSUJI_FQNAME}
#CMD ["/bin/sh", "-c", "sh"]
