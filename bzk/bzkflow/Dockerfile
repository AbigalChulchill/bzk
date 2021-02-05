FROM gradle:6.8-jdk11 AS build

COPY . /app
WORKDIR /app
RUN java -version


RUN gradle :bzkflow:bootJar --refresh-dependencies 
FROM springci/graalvm-ce:master-java11
RUN java -version
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-cp","app:app/lib/*","net.bzk.flow.BzkflowApplication",">","/console.log"]