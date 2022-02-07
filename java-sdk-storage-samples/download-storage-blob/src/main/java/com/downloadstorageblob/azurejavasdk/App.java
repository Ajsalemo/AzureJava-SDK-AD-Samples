package com.downloadstorageblob.azurejavasdk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class App {
    private static final String storageAccountBlobName = System.getenv("AZURE_STORAGE_ACCOUNT_BLOB_NAME");
    private static final String storageAccountContainerName = System.getenv("AZURE_STORAGE_ACCOUNT_CONTAINER_NAME");
    private static final String storageAccountEndpoint = System.getenv("AZURE_STORAGE_ACCOUNT_ENDPOINT");
    private static final String dir = System.getenv("FILE_DIR");

    public static void downloadBlob(TokenCredential credential) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(storageAccountEndpoint)
                .credential(credential).buildClient();
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(storageAccountContainerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(storageAccountBlobName);
        int dataSize = (int) blobClient.getProperties().getBlobSize();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(dataSize);
        File directory = new File(dir);
        System.out.println(directory.getAbsolutePath());
        try {
            String fileName = blobClient.getBlobName();
            // This downloads the blob content to the /Files directory (also specified as an env var as FILE_DIR in this sample)
            // fileName is the name of the blob being downloaded stored as a variable, and saving it as such locally
            blobClient.downloadToFile(directory.getAbsolutePath() + File.separator + fileName);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Reference -
            // https://docs.microsoft.com/en-us/java/api/overview/azure/storage-blob-readme?view=azure-java-stable#authenticate-with-azure-identity
            final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
            final TokenCredential credential = new DefaultAzureCredentialBuilder()
                    .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
                    .build();

            downloadBlob(credential);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
