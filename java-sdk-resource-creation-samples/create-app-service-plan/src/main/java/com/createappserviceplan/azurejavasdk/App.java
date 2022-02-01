package com.createappserviceplan.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appservice.models.OperatingSystem;
import com.azure.resourcemanager.appservice.models.PricingTier;

public class App {
    private static String appServicePlanName = System.getenv("AZURE_APP_SERVICE_PLAN_NAME");
    private static String rgName = System.getenv("AZURE_RESOURCE_GROUP_NAME");

    public static void createAppServicePlan(AzureResourceManager azureResourceManager) {
        System.out.println("Creating an App Service Plan with name: " + appServicePlanName);
        azureResourceManager.appServicePlans()
                .define(appServicePlanName)
                .withRegion(Region.US_EAST)
                .withExistingResourceGroup(rgName)
                .withPricingTier(PricingTier.PREMIUM_P1V2)
                .withOperatingSystem(OperatingSystem.LINUX)
                .create();
        System.out.println("Created an App Service Plan with name: " + appServicePlanName);
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
            // Invoke createAppServicePlan to create the App Service Plan
            createAppServicePlan(azureResourceManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
