FROM java:8-jdk-alpine
COPY ./build/libs/AccountsManager-1.0-SNAPSHOT-all.jar /usr/
COPY ./src/main/resources/myBatis.config /usr/
WORKDIR /usr/
# KIVID
CMD java -jar AccountsManager-1.0-SNAPSHOT-all.jar
EXPOSE 80