# Technical Task Application

## Introduction

This is a Dockerized RESTful API using Spring Boot that consumes an external REST API (JSONPlaceholder) to fetch data dynamically, stores it in a local database, and provides various endpoints for data retrieval and user authentication.


## Prerequisites

- Download and install SQL Server and SQL Server Management Studio (To create and run a database)
- Download and install Git on your system or IDE (To clone the project)
- Download and install maven on your system or IDE (to run mvn commands)
- Download and install Postman (For API testing)
- Download and install Docker (To run the container)


## Setup Instructions

### SQL Server Setup:

#### Running the server:

1 - Run SQL Server Configuration Manager
2 - Click on "SQL Server Services"
3 - Make sure SQL Server (MSSQLSERVER) is running

#### Enabling TCP/IP:

1 - Run SQL Server Configuration Manager
2 - Click on SQL Server Network Configuration
3 - From the dropdown menu, click on Protocols for MSSQLSERVER
4 - On the right menu, double click TCP/IP
5 - Make sure the "Enabled" field contains "Yes"
6 - Go to the "IP Addresses" tab
7 - Make sure IP11 has these options:

Active : Yes
Enabled : Yes
IP Address : 127.0.0.1
TCP Port : 1433

8 - Go back to the main menu and click on SQL Server Services
9 - Right click on SQL Server (MSSQLSERVER) and click restart

#### Creating a Database:

1 - Run Microsoft SQL Server Management Studio (SSMS)
2 - In "Server name" Choose "localhost"
3 - In "Authentication" Choose "Windows Authentication"
4 - Check "Trust server certificate" and click "Connect"
5 - From the "Object Explorer" right click "Databases" then "New Database". Choose a name and click OK.

#### Creating SQL Server Authentication Login Credentials:

1 - Run Microsoft SQL Server Management Studio (SSMS)
2 - Login using "Windows Authentication" just like previously
3 - From the "Object Explorer" right click on "Security" and "New" then "Login"
4 - Fill "Login name" with the value "bandar"
5 - Choose SQL Server authentication
6 - Fill "password" and "Confirm password" with the value "admin123"
7 - Go to "Server Roles" tab and check all the boxes (just in case) then press OK.


## Running the Docker container:

1 - From your system or IDE terminal execute the following command: git clone https://github.com/DawsariBandar/TechnicalTask.git
2 - Go to your IDE's terminal and execute these four commands:
"mvn clean"
"mvn package"
"docker build -t technical-task"
"docker-compose up --build"
3 - If the application ran successfully you will see a message similar to this: "Started TaskApplication in 3.92 seconds (process running for 4.212)"
4 - If you are facing problems with running the application with Docker then you can run it directly using the TaskApplication main class instead.

Note: if the application says that port 9090 is taken you have to do the following:
1 - Run CMD
2 - Type netstat -a -o -n and look for the PID of port 9090
3 - Type TASKKILL /F /PID "PID" //replace PID with the number you got from the previous step


## Testing API Endpoints

### Using Postman

1 - Launch Postman and click the plus button to create new tabs for each endpoint.
2 - In each tab you will have to fill out the following: Method, URL, Header (if needed), and Body (if needed).
3 - Click "Send" and you will see a response (200 OK or 403 Forbidden) with a Body containing the response data in case of successful request. 
--------------------------------------------------------------------------------------------------
#### Login
- Description: Authenticates users and returns a JWT that will be used in the header of the rest of the endpoints.
- URL: 'http://localhost:9090/api/login'
- Method: POST
- Body: 
 {
    "username": "admin",
    "password": "admin123"
  }

- Response:
{
  "jwtToken": "your-jwt-token"
}

Note: You have to use this exact username and password first to get a JWT and be able to fetch Users and Posts from JSONPlaceholder.
I went with this approach because in the beginning the database is going to be empty. Which will cause the user to not be able to login.
--------------------------------------------------------------------------------------------------
#### Fetch Data

- Description: Fetches data (Users and Posts) from the external API (In our case it is JSONPlaceholder ) and stores it in the database. And in case the data is a "User" also assign a random generated password to them in the database (to prevent it from being stored as NULL).
- URL: 'http://localhost:9090/api/fetch-data'
- Method: GET
- Header:
  Key: Authentication
  Value: Bearer <your-jwt-token>

- Response:
{
  "message": "Data fetched and stored successfully"
}
--------------------------------------------------------------------------------------------------
#### Retrieve All Data

- Description: Retrieves all data stored in the database.
- URL: 'http://localhost:9090/api/data'
- Method: GET
- Header:
  Key: Authentication
  Value: Bearer <your-jwt-token>

- Response:
[
  {
    "id": 1,
    "title": "Title 1",
    "body": "Body 1",
    "userId": 1
  },
  ...
]
--------------------------------------------------------------------------------------------------
#### Retrieve Data by ID

- Description: Retrieves data based on ID from the database.
- URL: 'http://localhost:9090/api/data/1' (replace 1 with any ID)
- Method: GET
- Header:
  Key: Authentication
  Value: Bearer <your-jwt-token>

- Response:
{
  "id": 1,
  "title": "Title 1",
  "body": "Body 1",
  "userId": 3
}
--------------------------------------------------------------------------------------------------

## Rate Limiting

The /api/fetch-data endpoint has rate limiting implemented to prevent abuse. Each user can make 10 rapid requests per minute. If the limit is exceeded, the server will respond with a 429 Too Many Requests status.

## Database Relation

The "Users" table has a one-to-many relation with the "Posts" table. Which is linked by the foreign key "userId" in "Posts" and primary key "id" in "Users"


Note: If you have any questions please don't hesitate to contact me on my email Aldawsaribandar@outlook.com






