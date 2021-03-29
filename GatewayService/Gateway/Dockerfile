from openjdk:8

EXPOSE 8080

#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean install



#ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/home/app/target/googledrive-service.jar"]

ADD target/gatewayservice.jar gatewayservice.jar

ENTRYPOINT ["java","-jar","/gatewayservice.jar"]