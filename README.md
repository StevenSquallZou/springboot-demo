# springboot-demo
This repo is an experimental space to try features of SpringBoot.

## Prerequisites:
- Install Java 8 or above
- Install MySQL in local machine, create the following databases during installation:
  - world
  - sakila
- Create DB users:
  - Create the username for "world" database:
    ```
    CREATE USER '<world_username>'@'%' IDENTIFIED BY '<world_password>';
    GRANT ALL PRIVILEGES ON world.* TO '<world_username>'@'%';
    ```

  - Create the username for "sakila" database:
    ```
    CREATE USER '<sakila_username>'@'%' IDENTIFIED BY '<salika_password>';
    GRANT ALL PRIVILEGES ON sakila.* TO '<sakila_username>'@'%';
    ```

  - Create the username used by application for all the databases:
    ```
    CREATE USER '<app_username>'@'%' IDENTIFIED BY '<app_password>';
    GRANT SELECT, INSERT, UPDATE, DELETE ON world.* TO 'app'@'%';
    GRANT SELECT, INSERT, UPDATE, DELETE ON sakila.* TO 'app'@'%';
    ```
- 
  FLUSH PRIVILEGES;
- Set up the following environment variables:
  - SPRING_PROFILES_ACTIVE: dev_group (or uat_group, prod_group)
  - BASE_JDBC_URL: the base JDBC URL for MySQL, e.g., `jdbc:mysql://localhost:3306`
  - APP_DB_USERNAME: the DB username used by app for all the databases
  - APP_DB_PASSWORD: the DB password used by app for all the databases
  - WORLD_DB_USERNAME: the DB username for the "world" database
  - WORLD_DB_PASSWORD: the DB password for the "world" database
  - SAKILA_DB_USERNAME: the DB username for the "sakila" database
  - SAKILA_DB_PASSWORD: the DB password for the "sakila" database
  - REDIS_HOST: the Redis host
  - REDIS_PORT: the Redis port (default is 6379)
  - REDIS_PASSWORD: the Redis password (if any)

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
  - HTTP GET: 
    - Query film text by film id based on mybatis/redis: http://localhost:8080/filmText/1
    - Query film text attribute by film id and attribute name based on mybatis/redis: http://localhost:8080/filmText?filmId=1&attributeName=title
  - HTTP POST: Create a new film text based on mybatis/redis: http://localhost:8080/filmText
- Spring MVC demo:
  - HTTP GET: http://localhost:8080/test