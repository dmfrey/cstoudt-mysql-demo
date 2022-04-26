# MySQL Demo Spring Boot Application

## About the Application

This is a basic Spring Boot application that implements database persistence with `Spring Data JDBC`. The code is all maintained in the `DemoApplication.java` file for ease of implementation and understanding.

At the core, there is a Java Record, called `Stock` that identifies a basic Stock ticker aggregate. It is an Entity that represents a table in the database.

Persistence activities on the `Stock` aggregate are performed through the `StockService` Java interface. Spring Data JDBC exposes a `CrudRepository` that supplies basic finders and modifiers, however, you have complete control over how this aggregate can be accessed and modified.
There is an example of a `@Modifying` query in there where you can override the SQL completely to your liking.
The test class `StockServiceTests` will exercise the custom queries, but it uses the H2 in-memory database to do so.

Accessing the `StockService` is performed through the `StockEndpoint`, which is a `@RestController` exposing REST endpoints.
These endpoints are accessible at `/api/stocks`. The test class `WebMvcTests` shows an example of how to test the API endpoints in isolation.
The `StockService` is mocked, so no actual database is required.

There is also one overall context test that validates the application is configured correctly. This is `DemoApplicationTests`.
When you run the application, you can then validate the health of the application by observing the Actuator at `http://localhost:8080/actuator/health`

There is also an integration test `MysqlDbIntegrationTests`, that will switch to the `mysql` profile and run the tests against a mysql database.

*NOTE:* MySQL need not be installed on the machine. It uses a test harness called [TestContainers](https://www.testcontainers.org/) which uses Docker internally to spin up the mysql docker container and run the tests.

Docker is required to run this test, so its been moved to a separate Maven profile (see testing below).

## Running the Application

### From the IDE

Simply in the IDE, right-click the `DemoApplication` and click Run.

### From the Command Line

From the command line, maven can run the application in it's default coniguration with H2 as the database.
```bash
$ ./mvnw spring-boot:run
```

If you want to switch to a running MySQL, you can switch the profile on the command line as well:
```bash
$ ./mvnw spring-boot:run -Dspring.profiles.active=mysql
```
*NOTE:* MySQL needs to be running prior to starting the app and running on localhost at the default port.

## Building the Application

On the command line, maven will build and test the application
```bash
$ ./mvnw clean package
```

You can also skip the tests to produce a faster build:
```bash
$ ./mvnw clean package -DskipTests=true
```

Once the application jar has been built, you can also execute the jar file from the command line
```bash
$ java -jar target/demo-0.0.1-SNAPSHOT.jar
```

You can target a running MySQL as follows:
```bash
$ java -jar target/demo-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=mysql
```
*NOTE:* MySQL needs to be running prior to starting the app and running on localhost at the default port.

## Testing the Application

### In the IDE
Each test class can be executed individually, all tests within a package, or all tests at the root of the packages. Simply right-click on whatever scope you're in and select Run tests.

### On the command line
Maven will run the tests from the command line as follows
```bash
$ ./mvnw clean test
```

If you want to run the MySQL integration test, you need to supply the Maven profile for the mysql test. Docker must be installed and running so it can leverage that environment to run the MySQL docker image.
```bash
$ ./mvnw clean package -P integration
```
*NOTE:* This will only run the integration tests, no other tests will execute as it's configured.

## Running on Tanzu Application Server

In the root of the project is a `manaifest.yml` that should allow an immediate `cf push`, so long as the Spring Boot Jar file has been built.


