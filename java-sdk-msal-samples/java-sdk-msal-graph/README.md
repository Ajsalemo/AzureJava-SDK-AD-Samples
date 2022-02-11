# MSAL Graph example

This sample was taken from [here](https://github.com/Azure-Samples/ms-identity-java-spring-tutorial/tree/main/2-Authorization-I/call-graph) but slimmed down and reduced.

Before running this sample make sure to [create and registry your Azure AD application](https://github.com/Azure-Samples/ms-identity-java-spring-tutorial/tree/main/2-Authorization-I/call-graph#register-the-webapp-app-java-spring-webapp-call-graph) that's used for authentication.
## Running the sample
- Replace needed environment variables in `application.yml`.
- Run `mvn clean package` to install the required dependencies and build the jar. 
- Run `java -jar target/msi-system-assigned-1.0-SNAPSHOT-jar-with-dependencies.jar` to run the sample.