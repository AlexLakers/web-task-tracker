#stage=0
FROM alpine:3.20 AS base
LABEL authors="alexlakers"


RUN apk add git openjdk17
RUN git clone https://github.com/AlexLakers/web-task-tracker.git
WORKDIR /web-task-tracker
RUN git checkout prod && ./mvnw package

RUN  wget https://dlcdn.apache.org/tomcat/tomcat-11/v11.0.2/bin/apache-tomcat-11.0.2.tar.gz \
        && tar -xvzf apache-tomcat-11.0.2.tar.gz \
        && rm apache-tomcat-11.0.2.tar.gz \
        && rm -rf apache-tomcat-11.0.2/webapps/*

#COPY task-manager-*.war .


RUN cp ./target/web-task-tracker-*.war /web-task-tracker/apache-tomcat-11.0.2/webapps/ROOT.war

#WORKDIR web-task-tracker
#RUN ./mvnw && cp target/web-task-tracker-*.war

#stage=1
FROM alpine:3.20 AS result
RUN apk add openjdk17

WORKDIR /web-task-tracker
COPY --from=base /web-task-tracker/apache-tomcat-11.0.2 ./apache-tomcat-11.0.2
EXPOSE 8080
ENTRYPOINT ["/web-task-tracker/apache-tomcat-11.0.2/bin/catalina.sh"]
CMD ["run"]