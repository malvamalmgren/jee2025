# jee2025

## Overview
This project is a CRUD-based RESTful web service for managing a collection of games using Jakarta EE 11 preview.\
Additional featuers include filtering and pagination.

## Features

### CRUD Operations
### [Filtering and pagination](#filtering-options) 
### [Jakarta Bean Validation](#validations)
### [Error Handling](#error-handling)
### Testing
Unit tests for service and mapper classes.\
*ArchUnit* ArchitectureTest.\
*RestAssured* API tests for REST-endpoints.
### Containerization
Dockerfile for the application, plus Docker Compose configuration with a database.

## Tech specification
* Jakarta EE 11 Preview
  * Jakarta Data (jakarta.data-api:1.0.1)
  * Jakarta Persistence (jakarta.persistence-api)
  * Jakarta Bean Validation (jakarta.validation-api)

* Maven
* Docker
* PostgreSQL

---

## Implementation
### Layers
**Game entity (JPA)**

**DTOs:**\
CreateGame, UpdateGame, and GameResponse for incoming/outgoing data.

**Repository:**\
GameRepository extends CrudRepository, also custom queries.

**Service:**\
GameService enforces business rules and validation checks.

**Mapper:**
Converts between Game and DTO objects.

**REST Resource:**\
Exposes endpoints for create, read, update, delete, and filtering.

### Attributes
id (auto-generated)\
title\
release\
publisher\
description (optional)\
genres\
price

### Validations
At least one genre is mandatory.\
The combination of title, release, and publisher must be unique.

### Filtering Options
* Ordered by release: e.g., /api/games/release
* By publisher: e.g., /api/games/publisher/Nintendo

---

## How to build and run the application
### Prerequisites
&#9758; Make sure you have Docker and Docker Compose installed\
&#9758; To build and run outside of Docker you need Java23 and Maven installed

### Docker step-by-step
1. Clone respository\
`git clone https://github.com/malvamalmgren/jee2025.git`
2. Locate root folder\
`cd jee2025`
3. Run services in docker-compose.yaml\
`docker-compose up --build`
4. Make sure containers are running\
`docker ps`
5. Check it out at:\
http://localhost:8080/api/games

---


