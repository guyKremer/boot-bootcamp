FROM java:8-jdk-alpine
COPY ./build/libs/bootcamp-1.0-SNAPSHOT-all.jar /usr/
COPY ./src/main/resources/server.config /usr/
COPY ./src/main/resources/queryResource.config /usr/
COPY ./src/main/resources/queryResource.config /usr/
COPY ./src/main/resources/indexingResource.config /usr/
WORKDIR /usr/
# KIVID
CMD java -jar bootcamp-1.0-SNAPSHOT-all.jar
EXPOSE 8001