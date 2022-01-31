package com.createresourcegroup.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;

public class App {
    private static String rgName = System.getenv("AZURE_RESOURCE_GROUP_NAME");

    public static void createResourceGroup(AzureResourceManager azureResourceManager) {
        System.out.println("Creating a resource group with name: " + rgName);
        azureResourceManager.resourceGroups().define(rgName)
                .withRegion(Region.US_EAST)
                .create();
        System.out.println("Created a resource group with name: " + rgName);
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
            // Invoke createResourceGroup to create the resource group
            createResourceGroup(azureResourceManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
