# Java Spark Web Application

- A RESTful web application built with Java and Spark framework for managing collectible items.
- Website with real-time collectible updates

This project demonstrates modern Java web development practices with a clean architecture and robust API design.

The architecture follows a layered approach to separate concerns and improve maintainability. At the top, the HTTP
client or browser interacts with the application, sending requests to the server. These requests are first handled by
the Spark framework, which is responsible for routing and mapping requests to the appropriate controller endpoints. The
RoutesInitializer sets up all the routes, connecting the HTTP layer with the controller layer.

The controller layer is responsible for handling incoming requests, parsing JSON input, validating data, and delegating
the processing to the service layer. For example, the UserApiController manages all user-related requests, ensuring that
input is correctly validated and that responses are formatted properly before sending them back to the client.

The service layer contains the core business logic of the application. Classes like UserService and UserServiceImpl
perform validations, handle transactions, and convert between DTOs and entity models. This layer ensures that the
business rules are applied consistently and abstracts the database operations from the controllers. It also manages the
logic for real-time updates, pushing changes for bids or collectible status to connected clients.

Beneath the service layer is the DAO (Data Access Object) layer, which directly interacts with the database. UserDao and
other DAO classes handle all CRUD operations, query building, and execution, while extending base classes that manage
database connections. This layer encapsulates all the database logic, providing a clean interface for the service layer
to use.

Finally, the database layer stores all persistent data. This project supports any SQL database, such as PostgreSQL,
MySQL, or H2, and all queries are executed through the DAO layer. By keeping the database interactions separate from
business logic and request handling, the application achieves a high degree of modularity and testability.

Overall, this project serves as an example of a well-structured Java web application, combining Spark framework routing,
controller-based request handling, service-layer business logic, DAO-driven database operations, SQL persistence, and
real-time updates for collectibles and bidding into a cohesive and maintainable system.

# Installation and setup
```bash
git clone https://github.com/Kaserola4/java-spark-web-app.git
cd ./java-spark-web-app/

mvn clean install
mvn exec:java

```
