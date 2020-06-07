# Question 1 - Billable Hours
This project consists of two main components and a supporting apidoc 

- [Backend API](#billablehour-api)
- [React Frontend](#billablehour-frontend)
- [API Documentation](#apidoc)
- [Postman Collection](https://documenter.getpostman.com/view/2659866/SztK2kQA)

<a name="billablehour-api"></a>
## Backend API

This represents the project backend built with Spring Boot + Kotlin

### Dependencies
1. Spring Boot 2.3
2. Java 11
3. Kotlin
4. Mysql Database 5.7
5. Gradle 6.3
6. IntelliJ IDE version 2020.1.2


### Setting up
Both frontend and API backend have been bundled in the same project to make it easier to set up. Follow the steps below to set up the API Backend given that you have all the dependencies.
1. Clone the repository
    ```bash
    git clone https://github.com/XIXIS/afi.git
    ```
2. Import as a gradle project into your IDE
3. Install Gradle Dependencies
4. Create mysql database and supply credentials in the application.properties
    ```properties
    spring.datasource.name={DB_NAME}
    spring.datasource.url=jdbc:mysql://localhost:3306/{DB_NAME}?useSSL=false
    spring.datasource.username={DB_USER}
    spring.datasource.password={DB_PASSWORD}
    ```
5. Run the main application class
    `BillableHoursApplication.kt`
    The application automatically runs on port 8080. Ensure the port if free before running the application.

<a name="billablehour-frontend"></a>
## React Frontend
This represents the frontend built with ReactJs

### Prerequisite
Application backend API has already been set up

### Dependencies
1. React 16.13.1
2. NodeJs
3. Webstorm IDE (recommended) or any other Javascript Supported IDE
4. Yarn 1.22.4

### Setting up
The frontend project directory already exists in the cloned repository done in the Backend API set up
1. Open `billablehour-frontend` folder into your IDE
2. `cd` into project directory
3. Rename `.env.example` to `.env` and fill out the API Base url
4. Install node dependencies by running the command below
    ```bash
    yarn install
    ```
5. Run the project in development mode using the command below
    `yarn start`. It should run on port 3000.
6. Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

<a name="apidoc"></a>
## API Documentation
This represents a generated API Documentation. It was generated with [apidocjs](https://apidocjs.com)

### Dependencies
1. PHP 5.4+

### Setting up
The frontend project directory already exists in the cloned repository done in the Backend API set up
1. `cd` into apidoc directory
2. Execute the following commands to serve the doc via PHP's inbuilt server 
    ```bash
    php -S locahost:[port]
    ```
3. Open [http://localhost:[port]](http://localhost:8000) to view it in the browser.

<a name="apidoc"></a>
## Postman Collection
[This](https://documenter.getpostman.com/view/2659866/SztK2kQA) is a postman collection
