NuBank: Code Challenge: Authorizer
The objective of this project was to create an application that authorizes a transaction for a specific account following a set of business rules.
In order to complete the challenges some assumptions were made:

Sample usage of the Authorizer
How the program should work?
Your program is going to receive json lines as input in the stdin and should provide
a json line output for each of the inputs, imagine this as a stream of events arriving at the
authorizer.
How the program should be executed?
Given a file called operations that contains many lines describing operations in json format 


$ git clone:  https://github.com

The code will be in srs/main package.

Console Input: Allows Gson parse the stdin JSON into the proper Object.
Main Class: Manages the main run of the project
Rules folder: manages the business rules using factory pattern
Service folder: service class
Account: Manages the Account Object logic
Transaction: Manages the Transaction Object Logic.
Run it
To run the project you can do two things:

Run a JAR file:
To do this navigate, in the command line
to the main folder of the project and run the following command:

cd out/artifacts
$ java -jar NubankTest.jar
Run it from Intellij IDEA
Open the project in Intellij IDEA. 
Then go to src/main and right-click over MainRun.java and Run As -> Application in Run/Debug Configuration.

Once is running in the command line the project will wait for every line and process it.

Prerequisites 
Java 11


Test it
There are 5 available. This files are available in src/test. 

In the src/test package are 2 files. One for Account testing and one for Transaction testing. 
The 5 available test are build in order to follow the business rules.

Built With
Java
Gson - Gson is a Java library that can be used to convert Java Objects into their JSON representation.