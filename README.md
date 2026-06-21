# code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

### ejemplo usando lambda
>- ProductoResource

###
>- ./mvnw quarkus:add-extension -Dextension="junit5-mockito"
>- ./mvnw test

### endpoint para probar product
>- curl -X GET http://localhost:8765/product/      -H "Accept: application/json"
>- curl -X GET http://localhost:8765/product/obtener/1      -H "Accept: application/json"
>- curl -X GET http://localhost:8765/product/custom/Luis      -H "Accept: application/json"
>- curl -X DELETE http://localhost:8765/product/1

### instalar resilience
>- ./mvnw quarkus:add-extension -Dextension="smallrye-fault-tolerance"

### endpoint para probar resilience
>- curl -X GET http://localhost:8765/resilient/test-circuit \
     -H "Accept: text/plain"
>- curl -X GET http://localhost:8765/resilient/circuit-breaker \
     -H "Accept: application/json"
>- curl -X GET http://localhost:8765/resilient/bulkhead \
     -H "Accept: application/json"
>- curl -X GET http://localhost:8765/resilient/rate-limiter \
     -H "Accept: application/json"
>- curl -X GET http://localhost:8765/resilient/retryApi \
     -H "Accept: application/json"

### endpoint para probar PayPal
>- curl -X POST http://localhost:8765/paypal/create-order \
     -H "Accept: text/plain"
>- curl -X GET "http://localhost:8765/paypal/success?token=1TT98985P1297633S" \
     -H "Accept: text/plain"
>- curl -X GET http://localhost:8765/paypal/cancel \
     -H "Accept: text/plain"

### endpoint para probar HilosController
>- curl -X GET http://localhost:8765/api/hilos/executor-service \
     -H "Accept: application/json"
>- curl -X GET http://localhost:8765/api/hilos/thread-pool-executor \
     -H "Accept: application/json"
>- curl -X GET http://localhost:8765/api/hilos/rxjava \
     -H "Accept: text/plain"

### endpoint para probar PageEventController (Messaging)
>- curl -X GET http://localhost:8765/api/publish/T4/Luis \
     -H "Accept: application/json"

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```
### https://developers.redhat.com/articles/2022/03/03/rest-api-error-modeling-quarkus-20#model_the_error_response
import com.fasterxml.jackson.annotation.JsonInclude;

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and Jakarta Persistence

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)



### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

### jakarta
https://blog.jetbrains.com/idea/2021/02/creating-a-simple-jakarta-persistence-application/

### agregar extensiones
./mvnw quarkus:add-extension -Dextension="quarkus-smallrye-openapi"

### ubicación de error
http://localhost:8765/q/swagger-ui/

### página web
http://localhost:8765/Product.html

### ubicación de swagger
http://localhost:8765/q/swaggerui/

## ruta de comando
https://github.com/agoncal/agoncal-course-quarkus-starting/blob/master/bootstrap.sh

```shell script
mvn -U io.quarkus:quarkus-maven-plugin:create \
-DprojectGroupId=org.agoncal.quarkus.starting \
-DprojectArtifactId=rest-book \
-DclassName="org.agoncal.quarkus.starting.BookResource" \
-Dpath="/api/books" \
-Dextensions="resteasy-jsonb" 
```

```shell script
mvn -U io.quarkus:quarkus-maven-plugin:create \
-DprojectGroupId=org.programacionparaaprender.quarkus.starting \
-DprojectArtifactId=rest-product \
-DclassName="org.programacionparaaprender.quarkus.starting.BookResource" \
-Dpath="/product" \
-Dextensions="resteasy-jsonb" 
```

```shell script
./mvnw quarkus:dev
```

```shell script
systemctl status docker
docker ps -a
docker image ls
```

### rutas
>- http://localhost:8765/product/
>- http://localhost:8765/product/obtener/1
>- http://localhost:8765/product/custom/Luis



# Proyecto Apache Kafka con Java 21

Este proyecto demuestra cómo usar Apache Kafka con Java 21, incluyendo características modernas como Virtual Threads.

### curso kafka
>- https://www.udemy.com/course/kafka-cluster-deployment-and-java-springboot/

## Requisitos Previos

1. Java 21 o superior
2. Maven 3.6+
3. Docker y Docker Compose (opcional, para Kafka local)
4. Git
5. modificar el server.properties la sección log.dirs=C:/kafka_2.13-2.8.0/data/kafka-logs
## Configuración del Proyecto

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd kafka-java21-project
```

### probar endpoint
>- http://localhost:8080/api/publish/T4/blog

#### login
