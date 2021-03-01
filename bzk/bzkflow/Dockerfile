FROM kirinddt/gradle-graalvm:latest AS build
 
ENV GRADLE_USER_HOME /path/to/custom/cache/dir
VOLUME $GRADLE_USER_HOME

COPY . /app
WORKDIR /app
#RUN java -version
RUN mkdir -p /bzk/model/flow/
RUN mv ./init-model/flow/* /bzk/model/flow/

RUN gradle :bzkflow:bootJar --refresh-dependencies 
#RUN ls -laR | grep bzkflow
FROM ghcr.io/graalvm/graalvm-ce:java11-20.3.1

EXPOSE 8080

RUN mkdir /app

COPY --from=build /app/bzkflow/build/libs/bzkflow-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app/spring-boot-application.jar"]
