from openjdk:8

EXPOSE 2222

#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean install

COPY tokens tokens

#ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/home/app/target/googledrive-service.jar"]

ADD target/fileservice-mysql.jar fileservice-mysql.jar

ENTRYPOINT ["java","-jar","/fileservice-mysql.jar"]