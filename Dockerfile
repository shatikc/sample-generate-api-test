FROM java:8
ARG JAR_PATH
ADD ${JAR_PATH} app.jar
EXPOSE 8080
CMD java - jar app.jar