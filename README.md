# web-task-tracker
This is a multi-page task managment app with athorization and authentication.
 It allows to make general task actions that depends on a role and permissions.
## Description
You as a user of this app can create your own tasks with different attriburtes, for example you can choise a specific task performer 
or to set task priority,status and so on. But maybe some user want to assigh his task to you for performing and it possible too.
This app represents web interface as a frontend part to interect with a server part of this app. 
Of course, the main priority was backend.You can use it for base to make something more or use out of box - it is your choise.

## About security.
As you see the url can be private or public and for checking it I used 'Filter' by jakarta.servlet.
It used for pre-checking account role too to evaluate a primary opportunity for using functional of this app(Controller checking).
After pre-checking will be performed a full-checking logged account and for it used special service which checks role and  
permissions in depending on the operation with task (Service checking).
For example, the updating task operation can perform only account as a role 'ADMIN'.
Moreover occurs the checking permissions of this role, in this case role should have permission 'UPDATE'.

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/16.png?raw=true)

## How does it works?
I want to tell you about web-task-tracker in more detail.
If you visit the main page you can see **the following buttons:** 'login',logout,'registration' which 
perorm corresponding action by button name.You should to know that these buttons can be hidden or not
and it depends on authentication process, for example,if some user finish authentication process successfully then 
he will see only 'logout' button and so on.
This app has the **fallowing URL-pathes:**
 '/' - public main page url,
 '/tasks'** - the url of a specific account private page,
'/tasks/delete','/tasks/update','tasks/create' - the private url set for different actions with tasks.
'/registration','/login','/logout' - the public url set for actions with user session and registration.

For example,some user decided to use this app. Firstly, he should to visit the main page and finished
all the steps for registration process.Pay attemption to this app have validation your input data and if you enter incorrect data thus you can see **corresponding errors.**

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/31.png?raw=true)

Secondary you as a authenticated user will be redirected to your own page with tasks.
In here you can see your own tasks and tasks which was assighned to you as a performer. This page also show tasks using dynamic filter.
You can enter corresponding params and **find all the tasks** using this params.

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/20.png?raw=true)

If you want to **create a new task** for execution you must fill the fallowing form:

![alt text](https://github.com/AlexLakers/ParserJsonCsvToXml/blob/master/WinFormsCsvJsonXml/App_Data/pictures/30.png?raw=true)

## About interaction with the database and transactions.
The interaction process with the database occurs using ORM Framework Hibernate that allows
to simplify the writing queries for intereaction with a specific database table using session functionality.
It also help us with mapping entities from database to java object and so on.
Unfortunately, this framework  doesn't have good instruments for transaction managment process.
Thus , I writed my own functional for using transaction.It is similar to the 'requqred propagation' behaviour.
The transaction is started in one service place and finished(commit,roolback) in the same place. 
If we don't have current open transaction then occurs oppening a new transaction else we use transaction 
that's opened early.The same logic used for cfinished transaction.I also used 'Thread Local' strategy for 
easy opening and reusing transaction.Pay your attemption to this app will be work correctly with one client 
becouse I used 'Thread Local' strategy for getting transaction.You cam use full-manual method to manage transactions.  


## About the project stucture and building:
This application is developed using MVC pattern.
  The servlets as a controller layer. 
  As a view level I used classic HTML + JSP pages.
  In this app I use web-filters to pre-handling all the input requests from user.
  Also I use DTO to interaction between different app layers and filter entity's search.
  As a database is choosed 'PostgreSQL' V 17.0
  As a building system I used 'Maven' V 3.6.3.
  As an application server I installed 'TOMCAT' V 11.0.2.
  As a system of conteiners I used 'Docker' 27.3.1 and wirted my own Dockerfile.
  java 17.0.11 2024-04-16 LTS.

You need to have all the necessary programms above.

You should control that the env var '$CATALINA_HOME' is seted.
It is a part of path to logging files. I used the next path:

```
export CATALINA_HOME=/opt/apache-tomcat-11.0.2.
```
After it you need use my ***Dockerfile*** /web-task-tracker/docker/Dockerfile.

You can build image by above Docker file using command:
```
docker build -f Dockerfile .
docker tag imageId web-task-tracker:1.0
```
You should ***create your own network and volume*** to improve container intercation:
```
docker network create backend-net
docker volume create db_data
```
Then, you need to create and run two conteiners.
The ***database-container***:
```
docker run --name database --mount type=volume,source=db_data,target=/var/lib/postgresql/data --network=backend-net -p 5632:5432 -e POSTGRES_USER=taskmanageradmin -e POSTGRES_DB=task_manager_repository -e POSTGRES_PASSWORD=taskManagerAdmin -d postgres:17
 ```
The ***app-container***(by Dockerfile-own image by alpine):
 ```
docker run --name app -p 8085:8080 -e HIBERNATE_CONNECTION_URL=jdbc:postgresql://database:5632/task_manager_repository -d --network=backend-net web-task-tracker:1.0
 ```
***Bellow you can see the refs to all the neccessary  lib:***

> [!IMPORTANT]
> - "Hibernate ORM Hibernate Core » 6.5.2.Final" https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core/6.5.2.Final
> - "Testcontainers Core 1.20.4" https://mvnrepository.com/artifact/org.testcontainers/testcontainers/1.20.4
> - "Jakarta Servlet 6.0.0" https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api/6.0.0
> - "PostgreSQL JDBC Driver » 42.7.3" https://mvnrepository.com/artifact/org.postgresql/postgresql/42.7.3
> - "Project Lombok 1.18.34" https://mvnrepository.com/artifact/org.projectlombok/lombok/1.18.34
> - ***The additional libs for unit-tests and logging:***
> - "Apache Log4j  1.2.17" https://mvnrepository.com/artifact/log4j/log4j/1.2.17
> - "JUnit Jupiter API 5.11.0" https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
> - "Mockito JUnit Jupiter 5.11.0" https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
> - "JUnit Jupiter Engine 5.11.0" https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
> - "JUnit Jupiter Params 5.11.0" https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
> - "Mockito Core 5.11.0" https://mvnrepository.com/artifact/org.mockito/mockito-core
> - ***And some libs for using jstl:***
> - "Jakarta Standard Tag Library Implementation 2.0.0" https://mvnrepository.com/artifact/org.mockito/mockito-core
> - "Jakarta Standard Tag Library API » 3.0.0" https://mvnrepository.com/artifact/jakarta.servlet.jsp.jstl/jakarta.servlet.jsp.jstl-api/3.0.0
