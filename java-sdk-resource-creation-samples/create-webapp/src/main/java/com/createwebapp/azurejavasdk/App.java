package com.createwebapp.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appservice.models.AppServicePlan;
import com.azure.resourcemanager.appservice.models.RuntimeStack;

public class App {
    private static final String webAppName = System.getenv("AZURE_WEB_APP_NAME");
    private static final String rgName = System.getenv("AZURE_RESOURCE_GROUP_NAME");
    private static final String appServicePlanName = System.getenv("AZURE_APP_SERVICE_PLAN_NAME");
    private static final String subscriptionId = System.getenv("AZURE_SUBSCRIPTION_ID");
    // This is in the form of
    // "/subscriptions/{AZURE_SUBSCRIPTION_ID}/resourceGroups/{AZURE_RESOURCE_GROUP_NAME}/providers/Microsoft.Web/serverfarms/{AZURE_APP_SERVICE_PLAN_NAME}"
    private static final String appServicePlanId = "/subscriptions/" + subscriptionId + "/resourceGroups/" + rgName + "/providers/Microsoft.Web/serverfarms/" + appServicePlanName;

    public static void createWebApp(AzureResourceManager azureResourceManager, AppServicePlan appServicePlan) {
        System.out.println("Creating a web app with name: " + webAppName);
        azureResourceManager.webApps()
                .define(webAppName)
                .withExistingLinuxPlan(appServicePlan)
                .withExistingResourceGroup(rgName)
                .withBuiltInImage(RuntimeStack.JAVA_11_JAVA11)
                .create();
        System.out.println("Created a web app with name: " + webAppName);
    }

    public static void main(String[] args) {
        try {
            final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
            final TokenCredential credential = new DefaultAzureCredentialBuilder()
                    .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
                    .build();

            AzureResourceManager azureResourceManager = AzureResourceManager
                    .configure()
                    .withLogLevel(HttpLogDetailLevel.BASIC)
                    .authenticate(credential, profile)
                    .withDefaultSubscription();
            // An existing App Service Plan name needs to be accessed from azureResourceManager (AzureResourceManager)
            AppServicePlan appServicePlan = azureResourceManager.appServicePlans().getById(appServicePlanId);
            // Invoke createWebApp to create the web app
            createWebApp(azureResourceManager, appServicePlan);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
