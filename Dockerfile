FROM openjdk:11-jre-slim
EXPOSE 8080
ARG JAR_FILE=target/starwars-*.jar
ADD ${JAR_FILE} starwars-api.jar
ENTRYPOINT ["java","-jar","/starwars-api.jar"]