# Atlanta Movie
The Atlanta Movie Project is a simple CRUD application built for final project of Georgia Tech's CS 4400 Intro to Database Systems Course.

This course provides students with an introduction to database design and use, and requires students to develop their own application beginning from a set of project requirements, creating an entity relationship diagram and relational schema from those requirements, and creating a full stack application from the diagram and schema.

The Atlanta Movie application is an online system for managing users, theaters, and movies in an application similar to Fandango. Users can register as customers, theater managers, or both. System admins are responsible for creating new theaters, movies, and managing users.
# Stack
- DB: MySQL
- Back-End: Spring Boot
- Front-End: ReactJS, Redux

# Installing the Backend Locally
1. Clone this repository
2. Make sure you have a MySQL server running containing correct table schema and data - enter your MySQL schema name and credentials in `application.resources`
3. Either open the project in IntelliJ or some other IDE, or navigate to the `src` directory in the code
4. To start the server, run `./gradlew bootRun` - API endpoints can then be reach; the base path is `localhost:8080/api/v1`
5. To access Swagger API documentation, enter `localhost:8080/api/v1/swagger-ui.html` in your browser - the server must be running in order to reach the API docs
