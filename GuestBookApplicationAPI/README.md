# API Setup
  Step 1: Use 'git clone' to clone this repository in you system.
  
  Step 2: Need to setup database before running API. Replace datasource details in application.yml file (url, username, password, databasename). Execute the sql statements from GuestBookApp_SQLScripts.sql to create required structure.
  
  Step 3: Use 'mvn clean install' to build the jar file (jar file you can find in target folder).
  
  Step 4: Go to target target folder and open command promt. Type 'java -jar guest-book-api-0.0.1-SNAPSHOT.jar' to start the application.
  
  Step 5: If everything went well, APi will start successfully and ready to use.
  
# Running test cases
Go to project root folder where test  classes are present and run command 'mvn -Dtest=GuestBookApiApplicationTests test'

# Creating Admin credentials
For creating admin entry, create one user from application and update its role to ADMIN, SQL statement is given in GuestBookApp_SQLScripts.sql file.
Create User by registering into application.
  

  

