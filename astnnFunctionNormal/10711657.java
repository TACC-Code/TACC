class BackupThread extends Thread {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter Licensee Name[Ohioedge]: ");
            String licenseeName = br.readLine();
            if (licenseeName == null || licenseeName.trim().equals("")) {
                licenseeName = "Ohioedge";
            }
            System.out.println("You entered licenseeName:" + licenseeName);
            java.sql.Date expirationDate = null;
            System.out.print("Enter License Expiration Date[2003-12-31]: ");
            String strExpirationDate = br.readLine();
            if (strExpirationDate == null || strExpirationDate.trim().equals("")) {
                expirationDate = java.sql.Date.valueOf("2003-12-31");
            } else {
                expirationDate = java.sql.Date.valueOf(strExpirationDate);
            }
            System.out.println("You entered expirationDate:" + expirationDate);
            System.out.print("Enter number of organizations[1]: ");
            String numberOfOrganizations = br.readLine();
            if (numberOfOrganizations == null || numberOfOrganizations.trim().equals("")) {
                numberOfOrganizations = "1";
            }
            System.out.println("You entered numberOfOrganizations:" + numberOfOrganizations);
            System.out.print("Enter number of users[1000]: ");
            String numberOfUsers = br.readLine();
            if (numberOfUsers == null || numberOfUsers.trim().equals("")) {
                numberOfUsers = "1000";
            }
            System.out.println("You entered numberOfUsers:" + numberOfUsers);
            File licenseDirectory = null;
            System.out.print("Enter the directory location where the license files will be created. You need to have read/write privilege on this directory. [C:\\ohioedge\\crm\\license]:");
            String strLicenseDirectory = br.readLine();
            if (strLicenseDirectory == null || strLicenseDirectory.trim().equals("")) {
                strLicenseDirectory = "C:\\ohioedge\\crm\\license";
            }
            licenseDirectory = new File(strLicenseDirectory);
            licenseDirectory.mkdirs();
            File publicKeyFile = new File(licenseDirectory, "public.key");
            publicKeyFile.createNewFile();
            File privateKeyFile = new File(licenseDirectory, "private.key");
            privateKeyFile.createNewFile();
            File msgSignatureFile = new File(licenseDirectory, "msg.signature");
            msgSignatureFile.createNewFile();
            System.out.println("You entered directory:" + licenseDirectory);
            System.out.print("1. Generating message...");
            String message = licenseeName + expirationDate.toString() + numberOfOrganizations + numberOfUsers;
            System.out.println("Complete.");
            System.out.print("2. Creating license files...");
            LicenseGenerator.generateKeyPair(publicKeyFile.toString(), privateKeyFile.toString());
            LicenseGenerator l = new LicenseGenerator(message, privateKeyFile.toString(), msgSignatureFile.toString());
            System.out.println("Complete.\npublic.key, private.key and msg.signature files are created in " + strLicenseDirectory + " directory.");
        } catch (Exception e) {
            System.out.println("Exception while generating license:" + e.toString());
        }
    }
}
