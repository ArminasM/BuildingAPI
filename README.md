# BuildingAPI

REST API for a simple real estate registry for buildings.

## Documentation

[Swagger](https://app.swaggerhub.com/apis-docs/ArminasM/ArminasBuildings/0.2)

## Running the application

```bash
./gradlew
./gradlew bootRun
```
After executing the bootRun command the program should be running.

## Running tests

```bash
./gradlew test
```

## Testing manually

Please use either [Postman](https://www.postman.com/) or [Swagger inspector](https://swagger.io/tools/swagger-inspector/) for requests.
You might need to add their extensions if using chrome. However, you may download [Postman](https://www.postman.com/) client.

### Comments

The app uses temporary in-memory h2 database, you can access the console [Here](http://localhost:8080/h2-console/login.jsp) during runtime.

The username: **sa**

The password(empty):

Driver Class: **org.h2.Driver**

JDBC url: **jdbc:h2:mem:test**

