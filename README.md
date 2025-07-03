# springboot-demo
This repo is an experimental space to try features of SpringBoot.

## Start Command:
    java -jar C:\MyWorkspaces\IntelliJ\springboot-demo\target\springboot-demo-1.0-SNAPSHOT.jar

## Features:
- RESTful demo APIs: 
  - HTTP GET: Default param: http://localhost:8080/hello
  - HTTP GET: Specific content param: http://localhost:8080/hello?content=Steven
- RESTful country APIs:
  - HTTP GET: Query by country name: http://localhost:8080/country?name=China
- RESTful actor APIs:
  - HTTP GET: Query by actor id: http://localhost:8080/actor/1
  - HTTP GET: Query by actor first name or last name: http://localhost:8080/actor?name=NICK
  - HTTP PUT: update actor: http://localhost:8080/actor