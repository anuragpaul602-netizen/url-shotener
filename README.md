URL Shortener

A scalable URL Shortener service built using Java, Spring Boot, PostgreSQL, and Redis.
The application allows users to shorten long URLs, create custom short links, track click analytics, and redirect users efficiently using HTTP redirects.

Features

Shorten long URLs into compact links

Custom short URLs (user-defined aliases)

Automatic short URL generation using Base62 encoding

Click tracking analytics

HTTP 302 redirects

Redis caching for faster URL lookups

Expiration support for links

REST API architecture

Tech Stack

Backend

Java

Spring Boot

Spring Data JPA

Maven

Database

PostgreSQL

Caching

Redis

Tools

IntelliJ IDEA

Postman

Git

GitHub

System Architecture
Client
  ↓
Spring Boot REST API
  ↓
Redis Cache
  ↓
PostgreSQL Database

Architecture Diagram:

<img width="959" height="638" alt="image" src="https://github.com/user-attachments/assets/961b81a9-88a8-41f9-8e23-a7887c53889c" />

Flow:

User sends long URL

API generates short code

Mapping stored in PostgreSQL

Redis caches frequently accessed URLs

Short URL redirects to original link

API Endpoints
Create Short URL

POST

/shorten
Request Body
{
  "url": "https://google.com",
  "customCode": "google"
}
Response
http://localhost:8080/google

If customCode is empty, the system automatically generates one.

Redirect to Original URL

GET

/{shortCode}

Example:

http://localhost:8080/google

Redirects to:

https://google.com

Uses HTTP 302 redirect.

Click Analytics

Each time a short URL is accessed, the system increments a counter.

Database example:

short_code	long_url	clicks
google	https://google.com
	5
Database Schema

Table: url_mapping

Column	Type
id	BIGINT
long_url	TEXT
short_code	VARCHAR
clicks	INT
expiry_date	TIMESTAMP
Running the Project Locally
1 Clone the Repository
git clone https://github.com/anuragpaul602-netizen/url-shortener.git
cd url-shortener
2 Setup PostgreSQL

Create database:

CREATE DATABASE url_shortener;

Update application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener
spring.datasource.username=your_username
spring.datasource.password=your_password
3 Start Redis

Mac:

brew install redis
brew services start redis

Verify:

redis-cli ping

Expected response:

PONG
4 Run Spring Boot Application
mvn spring-boot:run

Server starts at:

http://localhost:8080
Example Workflow

1. Create short URL

Request:

POST /shorten
{
  "url": "https://spring.io",
  "customCode": "spring"
}

Response:

http://localhost:8080/spring

2. Open the short URL

http://localhost:8080/spring

Redirects to:

https://spring.io

3. Database update

clicks = clicks + 1
Future Improvements

Rate limiting

Distributed ID generation

Docker deployment

Kubernetes scaling

Monitoring with Prometheus & Grafana

API documentation with Swagger

Project Structure
url-shortener
 ├── src
 │   ├── controller
 │   ├── service
 │   ├── repository
 │   ├── model
 │   ├── dto
 │   ├── util
 │   └── config
 ├── pom.xml
 ├── README.md
 └── .gitignore
