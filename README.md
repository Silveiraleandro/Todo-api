To-Do & File Storage REST API

Spring Boot REST API with two modules:
To-Do API — CRUD for tasks (filter by completion or tag)
File Storage API — upload, list, download, and delete local files

Tech stack
Java 17 • Spring Boot • Spring Web • Spring Data JPA • H2 (dev) • springdoc-openapi (Swagger)

Features
To-Do API

Create, Read, Update, Delete tasks

Filter by completed=true|false or tag=<tag>

H2 in-memory DB for dev

Proper HTTP codes: 201 on create, 200 on update, 204 on delete

File Storage API

Upload via multipart/form-data (Swagger file picker)

Stores files under uploads/ with a UUID storedName to avoid collisions

List metadata (fileName, storedName, size, uploadTime, contentType)

Download by storedName (served with original filename)

Delete by storedName

JSON error responses via @ControllerAdvice (400/404/413/423/500)

Configurable upload limits

Run locally (no Docker)
# From project root
mvn spring-boot:run
# or
mvn -DskipTests clean package
java -jar target/todo-api-1.0-SNAPSHOT.jar


App: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui.html
 (or /swagger-ui/index.html)
H2 Console: http://localhost:8080/h2-console

JDBC: jdbc:h2:mem:todo_db

User: sa / Password: (blank)

If you prefer IntelliJ only: run TodoApiApplication or Maven plugin spring-boot:run from the Maven tool window.

Run with Docker
Option A — Buildpacks (no Dockerfile)
mvn spring-boot:build-image
docker run -p 8080:8080 -v "%cd%/uploads:/app/uploads" todo-api:1.0-SNAPSHOT

Option B — Dockerfile
docker build -t todo-api:latest .
docker run -p 8080:8080 -v "%cd%/uploads:/app/uploads" todo-api:latest


The -v bind mount persists uploaded files across container restarts.

Configuration

src/main/resources/application.properties

# H2 & JPA
spring.datasource.url=jdbc:h2:mem:todo_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update

# Swagger (springdoc)
springdoc.api-docs.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html

# File storage
storage.location=uploads

# Upload limits (adjust as needed)
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

API Endpoints
To-Do

Create Task (201 Created)

curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Learn Spring Boot\",\"description\":\"Follow Leandro repo\",\"completed\":false}"


Get All Tasks

curl http://localhost:8080/api/tasks


Filter by completion

curl "http://localhost:8080/api/tasks?completed=true"


Filter by tag

curl "http://localhost:8080/api/tasks?tag=urgent"


Get by ID

curl http://localhost:8080/api/tasks/1


Update

curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Updated title\",\"completed\":true}"


Delete (204 No Content)

curl -X DELETE http://localhost:8080/api/tasks/1

Files

Note: responses include storedName (UUID prefix). Use it for download/delete.

Upload (multipart/form-data)

# Windows CMD
curl -X POST http://localhost:8080/api/files/upload -F "file=@C:\path\to\file.txt"
# PowerShell (quote carefully)
curl -Method POST -Uri http://localhost:8080/api/files/upload -Form @{file=Get-Item "C:\path\to\file.txt"}
# macOS/Linux
curl -X POST http://localhost:8080/api/files/upload -F "file=@/path/to/file.txt"


List

curl http://localhost:8080/api/files


Download by storedName (saves with original filename)

# -O -J uses server-provided filename
curl -O -J http://localhost:8080/api/files/<storedName>


Delete by storedName

curl -X DELETE http://localhost:8080/api/files/<storedName>


Common errors (JSON):

404 — file not found ({"status":404,...})

413 — file too large

423 — file locked/in use (Windows)

Development Notes

storedName = <UUID>_<originalName> prevents collisions and traversal.

For Windows path issues, path normalization and checks are implemented on read/delete.

Root URL / can be redirected to Swagger by adding a tiny controller:

@Controller class HomeController {
  @GetMapping("/") String home() { return "redirect:/swagger-ui.html"; }
}

Tests

Run:

mvn test


Coverage highlights:

TaskService unit tests (CRUD path, not-found)

TaskController WebMvc tests (201 Created + Location header)

FileStorageService tests with a temp directory (store/load/delete)

FileController WebMvc tests (multipart upload, download headers, delete)

License / Copyright

© 2025 Leandro Silveira. All rights reserved.
Swagger “license” line displays this via OpenAPI config (see OpenApiConfig).
