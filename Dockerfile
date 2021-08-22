FROM adoptopenjdk/openjdk11:armv7l-centos-jdk-11.0.6_10-slim as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk11:armv7l-centos-jdk-11.0.6_10-slim
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# set environment variable for TNS_ADMIN
ENV TNS_ADMIN=/app/db/wallet

ENTRYPOINT ["java","-cp","app:app/lib/*","dev.trangelier.hr.HrApplication"]