package com.deletestoragecontainer.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class App {
    private static final String storageAccountEndpoint = System.getenv("AZURE_STORAGE_ACCOUNT_ENDPOINT");
    private static final String storageAccountContainerName = System.getenv("AZURE_STORAGE_ACCOUNT_CONTAINER_NAME");

    public static void deleteContainer(TokenCredential credential) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(storageAccountEndpoint)
                .credential(credential).buildClient();
        BlobContainerClient blobContainerClient = blobServiceClient
                .getBlobContainerClient(storageAccountContainerName);
        System.out.println("Deleting container named " + storageAccountContainerName);
        blobContainerClient.delete();
    }

    public static void main(String[] args) {
        try {
            // Reference -
            // https://docs.microsoft.com/en-us/java/api/overview/azure/storage-blob-readme?view=azure-java-stable#authenticate-with-azure-identity
            final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
            final TokenCredential credential = new DefaultAzureCredentialBuilder()
                    .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
                    .build();

            deleteContainer(credential);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
