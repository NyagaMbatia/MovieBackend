# Movie Backend Application - Spring Boot

### Overview
This is a Spring Boot backend application for managing movie data, providing RESTful APIs for movie-related operations. The application supports basic CRUD operations, search functionality, and can be extended for features like user ratings, reviews, and recommendations.

### Features

##### Movie Management:
* Create, read, update, and delete movies
* Search movies by title, genre, year.
* Filter movies by various criteria

##### API Documentation:
* [Swagger/OpenAPI documentation]((http://localhost:8080/swagger-ui.html))
* [OpenAPI JSON](http://localhost:8080/v3/api-docs)

### Technologies Used

* Backend: Java 21, Spring Boot 3.4.4
* Database: PostgreSQL/MySQL (configurable)
* API Documentation: Swagger UI
* Build Tool: Maven
* Testing: JUnit 5, Mockito, Testcontainers
* Other: Lombok, MapStruct (for DTO mapping)

### Prerequisites
* Java JDK 21
* Maven 3.6.3 or higher
* PostgreSQL 15+ or MySQL 8+ (or Docker for containerized DB)
* IDE (IntelliJ IDEA, Eclipse or VS Code recommended)

### Installation & Setup
Clone the repository: 
* `git clone https://github.com/yourusername/movie-backend.git` 
* `cd movie-backend`

### Configure database:

Update application.properties or application.yml with your database credentials 
Or use Docker to start a database container (see Docker section below)

### Build the application: 
* mvn clean install
* Run the application: mvn spring-boot:run

### Docker Setup
The application can be run with Docker:

* Build the Docker image: ` docker-compose build`
* Start the services: ` docker-compose up`

This will start: 
* The Spring Boot application
* PostgreSQL database
* (Optional) pgAdmin for database management

## Configuration
Key configuration options (in application.properties or application.yml):
### Database
* spring.datasource.url=jdbc:postgresql://localhost:5432/moviedb
* spring.datasource.username=postgres
* spring.datasource.password=password

### JPA/Hibernate
* spring.jpa.hibernate.ddl-auto=update
* spring.jpa.show-sql=true

### Server
* server.port=8080

## Testing
To run tests: `mvn test`
// Integration tests with Testcontainers are included for repository layers.

Project Structure
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── movieapp/
│   │               ├── config/       # Configuration classes
│   │               ├── controller/   # REST controllers
│   │               ├── dto/          # Data Transfer Objects
│   │               ├── exception/    # Exception handling
│   │               ├── model/        # Entity classes
│   │               ├── repository/   # JPA repositories
│   │               ├── service/      # Business logic
│   │               └── MovieAppApplication.java
│   └── resources/
│       ├── application.properties    # Configuration
│       └── data.sql                 # Initial data (optional)
└── test/                            # Test classes
### Sample API Endpoints
* GET /api/movies - Get all movies
* GET /api/movies/{id} - Get movie by ID
* POST /api/movies - Create a new movie
* PUT /api/movies/{id} - Update a movie
* DELETE /api/movies/{id} - Delete a movie
* GET /api/movies/search?title={title} - Search movies by title
* GET /api/genres - Get all genres

Contributing: Contributions are welcome! Please follow these steps:

Fork the repository

Create a feature branch (git checkout -b feature/your-feature)

Commit your changes (git commit -m 'Add some feature')

Push to the branch (git push origin feature/your-feature)

Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contact
For questions or support, please contact:

Joe Mbatia - joembatiadev@gmail.com
Project Link: https://github.com/yourusername/movie-backend
