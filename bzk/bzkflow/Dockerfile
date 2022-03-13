FROM ghcr.io/graalvm/graalvm-ce:java11-20.3.1
 


COPY . /app
WORKDIR /app
#RUN java -version
RUN mkdir -p /bzk/model/

RUN cp -r ./init-model/* /bzk/model/


#RUN gradle :bzkflow:bootJar --refresh-dependencies



#ENV GRADLE_USER_HOME /path/to/custom/cache/dir
#VOLUME $GRADLE_USER_HOME

# EXPOSE 8080
EXPOSE 8080 


ENTRYPOINT ["sh","gradlew",":bzkflow:bootRun","--args='--spring.profiles.active=prod'","--daemon"]
#ENTRYPOINT ["java","-Xmx2304m","-Xms512m","-Dspring.profiles.active=prod","-jar","/app/spring-boot-application.jar"]
#ENTRYPOINT ["java","-Dspring.profiles.active=prod","-Dcom.sun.management.jmxremote","-Dcom.sun.management.jmxremote.port=7091","-Dcom.sun.management.jmxremote.rmi.port=7091","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.ssl=false","-Djava.rmi.server.hostname=localhost","-jar","/app/spring-boot-application.jar"]

