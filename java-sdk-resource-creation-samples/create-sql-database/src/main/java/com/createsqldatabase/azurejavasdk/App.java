package com.createsqldatabase.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.sql.models.SqlServer;

public class App {
    private static final String sqlServerName = System.getenv("AZURE_SQL_SERVER_NAME");
    private static final String sqlDatabaseName = System.getenv("AZURE_SQL_DATABASE_NAME");
    private static final String rgName = System.getenv("AZURE_RESOURCE_GROUP_NAME");
    private static final String administratorLogin = System.getenv("AZURE_SQL_SERVER_ADMIN_LOGIN");
    private static final String administratorPassword = System.getenv("AZURE_SQL_SERVER_ADMIN_PASSWORD");

    public static void createSqlServerAndDatabase(AzureResourceManager azureResourceManager) {
        System.out.println("Creating a server named " + sqlServerName);
        SqlServer sqlServer = azureResourceManager.sqlServers().define(sqlServerName)
                .withRegion(Region.US_EAST)
                .withNewResourceGroup(rgName)
                .withAdministratorLogin(administratorLogin)
                .withAdministratorPassword(administratorPassword)
                .create();
        System.out.println("Created a server named " + sqlServerName);
        System.out.println("Creating a database named " + sqlDatabaseName);
        sqlServer.databases()
                .define(sqlDatabaseName)
                .create();
        System.out.println("Created a database named " + sqlDatabaseName);
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

            createSqlServerAndDatabase(azureResourceManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
