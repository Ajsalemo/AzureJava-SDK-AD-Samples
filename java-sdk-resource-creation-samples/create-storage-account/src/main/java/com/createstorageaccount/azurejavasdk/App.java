package com.createstorageaccount.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;

public class App {
    private static final String storageAccountName = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
    private static final String rgName = System.getenv("AZURE_RESOURCE_GROUP_NAME");

    public static void createStorageAccount(AzureResourceManager azureResourceManager) {
        System.out.println("Creating a Storage Account named " + storageAccountName);
        azureResourceManager.storageAccounts()
                .define(storageAccountName)
                .withRegion(Region.US_EAST)
                .withNewResourceGroup(rgName)
                .create();
        System.out.println("Created a Storage Account named " + storageAccountName);
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

            createStorageAccount(azureResourceManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
