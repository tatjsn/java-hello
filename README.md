# HelloTj

How to start the HelloTj application
---
1. Run `cd soy && make` to build templages
1. Run `./mvnw package --update-snapshots` to build your application
1. Start application with `java -jar target/hello-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
