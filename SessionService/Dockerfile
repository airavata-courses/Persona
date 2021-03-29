from openjdk:8

EXPOSE 5555

#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean install



#ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/home/app/target/googledrive-service.jar"]

ADD target/sessionservice-mysql.jar sessionservice-mysql.jar

ENTRYPOINT ["java","-jar","/sessionservice-mysql.jar"]