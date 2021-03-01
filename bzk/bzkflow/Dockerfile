FROM kirinddt/gradle-graalvm:latest
 
ENV GRADLE_USER_HOME /path/to/custom/cache/dir
VOLUME $GRADLE_USER_HOME

COPY . /app
WORKDIR /app
#RUN java -version
RUN mkdir -p /bzk/model/flow/
RUN mv ./init-model/flow/* /bzk/model/flow/

RUN gradle :bzkflow:bootJar --refresh-dependencies 
#RUN ls -laR | grep bzkflow
#FROM springci/graalvm-ce:master-java11
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app/bzkflow/build/libs/bzkflow-0.0.1-SNAPSHOT.jar"]
