# Tenpo Challenge application

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

To start the application you will need installed:

- Java 11
- Docker
- Docker-compose

### Installing and Running

A step by step series of examples that tell you how to get a development environment running.

1. Build docker image
    On the root of the project execute on the CLI the command based on your Operative system.   

   * Windows:
      ```bash
      .\gradlew.bat build
      ```
   * Linux
      ```bash
      .\gradlew build
      ```

2. Start application
```bash
docker-compose up -d --build
```

### Test application
on the root of the repository is the postman collection that can be imported to test the API
