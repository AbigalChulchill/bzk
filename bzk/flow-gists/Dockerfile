FROM kirinddt/gradle-graalvm:latest 
VOLUME /tmp
COPY . /app
WORKDIR /app
#RUN java -version


RUN gradle :flow-gists:bootJar --refresh-dependencies 
#RUN ls -laR | grep bzkflow
#FROM springci/graalvm-ce:master-java11
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app/flow-gists/build/libs/flow-gists-0.0.1-SNAPSHOT.jar"]