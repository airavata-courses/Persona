from openjdk:8

EXPOSE 3333

ADD target/userservice-mysql.jar userservice-mysql.jar

ENTRYPOINT ["java","-jar","/userservice-mysql.jar"]