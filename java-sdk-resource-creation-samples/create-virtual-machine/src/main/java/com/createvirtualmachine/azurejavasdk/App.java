package com.createvirtualmachine.azurejavasdk;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.compute.models.KnownLinuxVirtualMachineImage;
import com.azure.resourcemanager.compute.models.VirtualMachine;
import com.azure.resourcemanager.compute.models.VirtualMachineSizeTypes;

public class App {
    private static final String linuxVirtualMacineName = System.getenv("AZURE_LINUX_VIRTUAL_MACHINE_NAME");
    private static final String rgName = System.getenv("AZURE_RESOURCE_GROUP_NAME");
    private static final String userName = System.getenv("AZURE_LINUX_VIRTUAL_MACHINE_USERNAME");
    private static final String password = System.getenv("AZURE_LINUX_VIRTUAL_MACHINE_PASSWORD");

    public static void createLinuxVirtualMachine(AzureResourceManager azureResourceManager) {
        System.out.println("Creating a Linux VM named " + linuxVirtualMacineName);

        VirtualMachine linuxVM = azureResourceManager.virtualMachines()
                .define(linuxVirtualMacineName)
                .withRegion(Region.US_EAST)
                .withExistingResourceGroup(rgName)
                .withNewPrimaryNetwork("10.0.0.0/28")
                .withPrimaryPrivateIPAddressDynamic() // This creates a subnet named "default" as well
                .withoutPrimaryPublicIPAddress()
                .withPopularLinuxImage(KnownLinuxVirtualMachineImage.UBUNTU_SERVER_18_04_LTS)
                .withRootUsername(userName)
                .withRootPassword(password)
                .withSize(VirtualMachineSizeTypes.fromString("Standard_D2a_v4"))
                .create();

        System.out.println("Created a Linux VM named " + linuxVirtualMacineName + " with ID: " + linuxVM.id());
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

            createLinuxVirtualMachine(azureResourceManager);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
