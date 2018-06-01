#### A SpringBoot AMQ Listener Producer Application 

A sample application that allowing quick access to an AMQ message server

Usage
-----

```sh
mvn clean install
mvn spring-boot:run

or 

java -jar target/spectrum-rest-activemq-poc-1.0.jar
```
This will create a fully functioning Application running as a SpringBoot Application
running locally, there is a url available where the swagger.json is available

http://localhost:8080/v2/api-docs

After installation the Swagger-UI will be locally accesible at
http://localhost:8080/swagger-ui.html

An implementation on OpenShift is available here
http://s2i-spring-boot-camel-amq-3scale-demo.193b.starter-ca-central-1.openshiftapps.com/swagger-ui.html
