FROM kirinddt/gradle-graalvm:latest
 
#ENV GRADLE_USER_HOME /path/to/custom/cache/dir
#VOLUME $GRADLE_USER_HOME

COPY . /app
WORKDIR /app
#RUN java -version


RUN gradle :flow-gists:bootJar --refresh-dependencies 
#RUN ls -laR | grep bzkflow
#FROM springci/graalvm-ce:master-java11
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app/flow-gists/build/libs/flow-gists-0.0.1-SNAPSHOT.jar"]