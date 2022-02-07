package com.uploadtoblobcontainer.azurejavasdk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class App {
    private static final String rgName = System.getenv("AZURE_RESOURCE_GROUP_NAME");
    private static final String storageAccountName = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
    private static final String storageBlobContainerName = System.getenv("AZURE_STORAGE_BLOB_CONTAINER_NAME");
    private static final String storageAccountEndpoint = System.getenv("AZURE_STORAGE_ACCOUNT_ENDPOINT");
    private static final String dir = System.getenv("FILE_DIR");
    private static final String id = UUID.randomUUID().toString();
    private static final String fileName = "log-rotate-" + id + ".txt";
    private static final String content = "This is a logfile written with id: " + id;

    public static void uploadToBlobContainer(TokenCredential credential) {
        // Reference - https://docs.microsoft.com/en-us/java/api/overview/azure/storage-blob-readme?view=azure-java-stable#create-a-blobserviceclient
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(storageAccountEndpoint)
                .credential(credential)
                .buildClient();
        BlobContainerClient blobContainerClient = blobServiceClient
                .getBlobContainerClient(storageBlobContainerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        File directory = new File(dir);

        try {
            File file = new File(directory.getAbsolutePath() + File.separator + fileName);
            try (Writer writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        blobClient.uploadFromFile(directory.getAbsolutePath() + File.separator + fileName);
    }

    public static void main(String[] args) {
        try {
            // Reference - https://docs.microsoft.com/en-us/java/api/overview/azure/storage-blob-readme?view=azure-java-stable#authenticate-with-azure-identity
            final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
            final TokenCredential credential = new DefaultAzureCredentialBuilder()
                    .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
                    .build();

            uploadToBlobContainer(credential);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
