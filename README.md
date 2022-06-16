# pai-cms

A simple system made for web application classes at PUT

# tech stack

- back-end:
  Java 17, Spring Boot, Hibernate Search (Apache Lucene), JMS, Postgres
- front-end:
  Angular (TypeScript) with Material

# Quickstart

Images for back-end and front-end are published on docker hub:

1. Go to ./quickstart
2. Run docker-compose up (or docker compose up depending on what version of Docker you're running)

# Build & execution from scratch

####Requirements for local build
- Angular CLI
- Java 17 & Maven

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

# Notes

Frontend application should be available on localhost:4200 with backend running on localhost:8080.

The fake-smtp mailbox should be available on localhost:5080

Please be aware, that the fake-smtp docker can take a considerable amount of time to startup properly, so it may take
some time for backend mailing to work.

# Functionalities overview

1. If you visit the main page (host:port/) you're prompted to sign in, register or change your password.
2. If you click on "Forgot your password?" you will be prompted to enter an email, if there is an account with that email registered, then an email will be sent (If you're using the provided docker-compose, then you can view all sent mails in fake-smtp mailbox on [host]:5080)
3. After signing in you can add new text content by clicking on the plus icon in upper-left corner
4. In the pop-up dialog you can set all content properties, as well as tag it by clicking on the chips
5. You can also optionally provide an image url - then it will be displayed on the left of the content card
6. By clicking on chips underneath the toolbar you can filter the results by selecting one or more tags
7. The content, it's title and subtitle is indexed, therefore you can search it using the search box (enter keyword and then press enter) 
8. Keywords do not need to match content exactly i.e. if you're looking for contents including "Shrek" you should get results if you use "Shreek" etc.
9. You can click on the share icon on a specific content card to open a pop-up dialog which allows you to enable sharing and generate an authorized public link
10. If you click on the three dots icon, you can either open an edit window or delete your content
11. You can sign out by clicking on your email in the upper right corner and selecting sign out


User session is managed by using JWTs, the Angular application uses http interceptor to automatically obtain new access token with a refresh token when it expires.