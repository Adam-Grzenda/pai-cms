# pai-cms
A simple system made for web application classes at PUT

# tech stack
- back-end:
  Java 17, Spring Boot, Hibernate Search (Apache Lucene), JMS, Postgres
- front-end:
  Angular (TypeScript) with Material

# Build & execution from scratch

1. Build front-end application:
   1. Go to ./frontend
   2. Run: ng build
   3. Build Docker: docker build -t pai-cms-frontend .
2. Build back-end application:
   1. Go to ./backend/cms-backend
   2. Compile: mvn compile
   3. Package to jar: mvn package
   4. Build Docker: docker build -t pai-cms-backend .
3. Execution:
   1. Go to the main catalog
   2. Run docker-compose up

Frontend application should be available on localhost:4200 with backend running on localhost:8080.

Please be aware, that the fake-smtp docker can take a considerable amount of time to startup properly, so it may take some time for backend mailing to work.