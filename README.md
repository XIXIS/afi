# Question 1 - Billable Hours
This project consists of two main components and a supporting apidoc 

- [billablehour-api](#dependencies)
- [billablehour-frontend](#setting-up)
- [apidoc](#ldap-setup)

<a name="billablehour-api"></a>
## billablehour-api

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

<a name="billable-frontend"></a>
## billable-frontend

This represents the frontend built with React


### Dependencies
--------------------------------------------
1. React 16.13.1
2. NodeJs 

### `yarn start`

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.


### `yarn build`

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

