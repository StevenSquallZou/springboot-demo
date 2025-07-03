# springboot-demo
This repo is an experimental space to try features of SpringBoot.

## Start Command:
    java -jar C:\MyWorkspaces\IntelliJ\springboot-demo\target\springboot-demo-1.0-SNAPSHOT.jar

## Features:
- RESTful demo APIs: 
  - HTTP GET: Default param: http://localhost:8080/hello
  - HTTP GET: Specific content param: http://localhost:8080/hello?content=Steven
- RESTful country APIs:
  - HTTP GET: Query by country name based on Spring JPA: http://localhost:8080/country?name=China
- RESTful actor APIs:
  - HTTP GET: Query by actor id based on Spring JPA: http://localhost:8080/actor/1
  - HTTP GET: Query by actor first name or last name based on Spring JPA: http://localhost:8080/actor?name=NICK
  - HTTP PUT: update actor based on Spring JPA: http://localhost:8080/actor
- RESTful film text APIs:
  - HTTP GET: Query film text by film id based on mybatis: http://localhost:8080/filmText/1
  - HTTP POST: Save a new film text based on mybatis: http://localhost:8080/filmText