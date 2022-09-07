class BackupThread extends Thread {
    public static void mainOLD(String[] args) throws Exception {
        boolean uploadDatabase = PromptsController.questionIsYesForYesNo(new ISFrame(), "Upload database", "Long upload");
        boolean latestJars = false;
        long msStart = DateTimeModel.getNowInMS();
        String version = "0.92";
        String baseServerDir = "/home/httpd/vhosts/patientos.org/httpdocs/forum_temp/";
        FileUtils.copyFile(new File("C:\\dev\\patientis\\output\\deploy\\patientis.ejb3"), new File("C:\\dev\\local\\deploy\\v092\\server\\appserver\\server\\default\\deploy\\patientis.ejb3"));
        FileUtils.copyFile(new File("C:\\dev\\patientis\\output\\deploy\\patientos.war"), new File("C:\\dev\\local\\deploy\\v092\\server\\appserver\\server\\default\\deploy\\patientos.war"));
        FileUtils.copyFile(new File("C:\\dev\\patientis\\output\\client\\patientis.jar"), new File("C:\\dev\\local\\deploy\\v092\\client\\lib\\patientis.jar"));
        FileUtils.copyFile(new File("C:\\dev\\patientis\\output\\client\\resources.jar"), new File("C:\\dev\\local\\deploy\\v092\\client\\lib\\resources.jar"));
        uploadFile(getClientLibZip(latestJars), baseServerDir + "upgrade-<version>-clientlib.zip".replace("<version>", version));
        uploadFile(getServerDeployZip(), baseServerDir + "upgrade-<version>-serverdeploy.zip".replace("<version>", version));
        if (uploadDatabase) {
            System.out.println("uploading database");
            uploadFile(getDatabaseZip(), baseServerDir + "upgrade-<version>-database.zip".replace("<version>", version));
        }
        System.out.println("upload complete in " + ((int) ((DateTimeModel.getNowInMS() - msStart) / 1000.0)));
        System.exit(1);
    }
}
