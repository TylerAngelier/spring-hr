FROM arm64v8/maven:3-openjdk-11-slim as build
WORKDIR /workspace/app

#COPY mvnw .
#COPY .mvn .mvn
COPY pom.xml .
COPY src src

#RUN ./mvnw install
RUN mvn install
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM arm64v8/maven:3-openjdk-11-slim

ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# set environment variable for TNS_ADMIN
ENV TNS_ADMIN=/app/db/wallet

ENTRYPOINT ["java","-cp","app:app/lib/*","dev.trangelier.hr.HrApplication"]