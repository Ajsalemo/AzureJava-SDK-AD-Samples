# System Assigned Identity

This sample uses the Azure Java SDK with [`DefaultAzureCredential`](https://docs.microsoft.com/en-us/azure/developer/java/sdk/identity-azure-hosted-auth#default-azure-credential) to use System Assigned Identity.

You can use this reference [here](https://docs.microsoft.com/en-us/java/api/overview/azure/identity-readme?view=azure-java-stable#authenticating-in-azure-with-managed-identity) for Managed Identity concepts. You can use this link [here](https://docs.microsoft.com/en-us/azure/app-service/overview-managed-identity?tabs=portal%2Cdotnet#add-a-system-assigned-identity) to set up a System Assigned Identity or [this link](https://docs.microsoft.com/en-us/azure/app-service/overview-managed-identity?tabs=portal%2Cdotnet#add-a-user-assigned-identity) to set up a User Assigned Identity.

## Running the sample
- Before running the sample, make sure your this sample is running on a resource that [supports Managed Identity](https://docs.microsoft.com/en-us/java/api/overview/azure/identity-readme?view=azure-java-stable#managed-identity-support).

> **NOTE**: If running on a resource such as Virtual Machine, you can use the below steps. Otherwise for App Services or related, the below doesn't apply and instead follow App Service (or related resource) documentation for deployment/getting started.
- Run `mvn clean package` to install the required dependencies and build the jar. 
- Run `java -jar target/msi-system-assigned-1.0-SNAPSHOT-jar-with-dependencies.jar` to run the sample.