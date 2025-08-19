To-Do & File Storage REST API

A simple Spring Boot REST API with two modules:

To-Do API → Manage tasks/notes with CRUD operations.

File Storage API → Upload, download, and manage files locally.

Features
To-Do API

Create, Read, Update, Delete tasks

Filter by tags or completion status

H2 in-memory database

Swagger/OpenAPI integration

File Storage API

Upload and download files

List stored files

Delete files

Configurable storage folder (uploads/)

Basic error handling (file not found, invalid uploads)

Build & Run
Run with Maven (no Docker)
mvn clean package
java -jar target/todo-api-0.0.1-SNAPSHOT.jar


The API will be available at:
👉 http://localhost:8080

Run with Docker

First, build the image:

docker build -t todo-api .


Run the container:

docker run -p 8080:8080 todo-api

API Endpoints
To-Do API

Create Task

curl -X POST http://localhost:8080/api/tasks \
-H "Content-Type: application/json" \
-d '{"title":"Learn Spring Boot","description":"Follow Leandro repo","completed":false}'


Get All Tasks

curl -X GET http://localhost:8080/api/tasks


Filter by completion

curl -X GET "http://localhost:8080/api/tasks?completed=true"

File Storage API

Upload a file

curl -X POST http://localhost:8080/api/files/upload \
-F "file=@/path/to/local/file.txt"


List files

curl -X GET http://localhost:8080/api/files


Download file

curl -O http://localhost:8080/api/files/{filename}


Delete file

curl -X DELETE http://localhost:8080/api/files/{filename}

Database

Uses H2 in-memory database (resets each restart).

Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:todo_db

Username: sa

Password: (empty)

Swagger / API Docs

Once running, check:
👉 http://localhost:8080/swagger-ui.html
