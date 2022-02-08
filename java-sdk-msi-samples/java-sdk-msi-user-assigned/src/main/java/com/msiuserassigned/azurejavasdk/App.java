package com.msiuserassigned.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

public class App {

    private static final String keyVaultName = System.getenv("AZURE_KEY_VAULT_NAME");
    private static final String keyVaultSecret = System.getenv("AZURE_KEY_VAULT_SECRET");
    private static final String msiUserIdentityClientId = System.getenv("AZURE_MSI_USER_CLIENT_ID");

    public static void retrieveKeyVaultSecret(TokenCredential credential) {
        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl("https://" + keyVaultName + ".vault.azure.net")
                .credential(credential)
                .buildClient();

        KeyVaultSecret bankSecret = secretClient.getSecret(keyVaultSecret);
        System.out.printf("Secret is returned with name %s and value %s \n", bankSecret.getName(),
                bankSecret.getValue());
    }

    public static void main(String[] args) {
        try {
            final TokenCredential credential = new DefaultAzureCredentialBuilder()
                    .managedIdentityClientId(msiUserIdentityClientId)
                    .build();

            retrieveKeyVaultSecret(credential);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
