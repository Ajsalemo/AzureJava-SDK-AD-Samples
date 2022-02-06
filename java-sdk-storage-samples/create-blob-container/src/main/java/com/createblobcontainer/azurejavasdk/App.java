package com.createblobcontainer.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.storage.models.PublicAccess;

public class App {
    private static final String rgName = System.getenv("AZURE_RESOURCE_GROUP_NAME");
    private static final String storageAccountName = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
    private static final String storageBlobContainerName = System.getenv("AZURE_STORAGE_BLOB_CONTAINER_NAME");

    public static void uploadToStorageContainer(AzureResourceManager azureResourceManager) {
        System.out.println("Creating a Blob Container with name " + storageBlobContainerName);
        azureResourceManager.storageBlobContainers().defineContainer(storageBlobContainerName)
                .withExistingStorageAccount(rgName, storageAccountName)
                .withPublicAccess(PublicAccess.CONTAINER)
                .create();
        System.out.println("Created a Blob Container with name " + storageBlobContainerName);
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

            uploadToStorageContainer(azureResourceManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
