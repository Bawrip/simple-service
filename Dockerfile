FROM gradle:5.5.1-jdk8 as builder
WORKDIR /source

ADD . /source
RUN gradle build -x Test

FROM openjdk:8u222-jre-stretch
WORKDIR /app/conf
COPY --from=builder /source/build/libs/tariff-1.0.0-SNAPSHOT.jar /app
CMD ["java", "-jar", "/app/tariff-1.0.0-SNAPSHOT.jar", "-Xmx200m"]
