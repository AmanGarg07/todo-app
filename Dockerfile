FROM openjdk:11-jre-slim
RUN mkdir /opt/app
COPY target/example-1.0-SNAPSHOT.jar /opt/app
COPY config.yml /opt/app
WORKDIR /opt/app
RUN java -version
CMD ["java", "-jar", "/opt/app/example-1.0-SNAPSHOT.jar", "server", "/opt/app/config.yml"]
EXPOSE 8080
