# Create App Service Plan

This sample uses the Azure Java SDK with [`DefaultAzureCredential`](https://docs.microsoft.com/en-us/azure/developer/java/sdk/identity-azure-hosted-auth#default-azure-credential) to create an App Service Plan.

Packages used:

[com.azure.resourcemanager](https://mvnrepository.com/artifact/com.azure.resourcemanager/azure-resourcemanager-resources) 
<br>
[azure-identity](https://mvnrepository.com/artifact/com.azure/azure-identity)

## Running the sample
- Before running the sample, make sure your local environment is set up correctly. You'll need to either create or use an existing Service Principal. You can follow this [link](https://docs.microsoft.com/en-us/azure/developer/java/sdk/get-started#set-up-authentication) on how to create a Service Principal.
When using this Java SDK, `DefaultAzureCredential` expects `AZURE_SUBSCRIPTION_ID`, `AZURE_CLIENT_ID`, `AZURE_CLIENT_SECRET` and `AZURE_TENANT_ID` to be available. This is called out [here](https://docs.microsoft.com/en-us/azure/developer/java/sdk/identity-azure-hosted-auth#configure-defaultazurecredential).
- Run `mvn clean package` to install the required dependencies. 
- Run `java -jar target/create-app-service-plan-1.0-SNAPSHOT-jar-with-dependencies.jar` to run the sample.