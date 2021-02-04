FROM oracle/graalvm-ce:20.1.0-java11
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
CMD ["java","-version"]
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-cp","app:app/lib/*","net.bzk.flow.BzkflowApplication",">","/console.log"]