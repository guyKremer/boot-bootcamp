FROM java:8-jdk-alpine
COPY ./build/libs/indexer-1.0-SNAPSHOT-all.jar /usr/
COPY ./src/main/resources/indexer.config /usr/
WORKDIR /usr/
CMD java -jar indexer-1.0-SNAPSHOT-all.jar
