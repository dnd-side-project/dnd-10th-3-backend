FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} dwy.jar
ENTRYPOINT ["java","-jar","/dwy.jar"]
