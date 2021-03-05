FROM openjdk:8-alpine

COPY target/uberjar/user-mst.jar /user-mst/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/user-mst/app.jar"]
