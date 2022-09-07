class BackupThread extends Thread {
    public void createResourceFiles() throws Exception {
        File resourcesDir = new File(routerConf, "resources");
        File datastoreDir = new File(resourcesDir, "datastore");
        if (!datastoreDir.exists()) {
            logger.info("Creating new datastore directory: " + datastoreDir.getAbsolutePath());
            datastoreDir.mkdirs();
        }
        if (!datastoreDir.isDirectory() || !datastoreDir.canWrite()) throw new Exception("Datastore directory invalid or unreadable: " + datastoreDir.getAbsolutePath());
        FileFilter filter = new FileFilter() {

            public boolean accept(File f) {
                return (f.getName().endsWith(".properties"));
            }
        };
        for (File f : datastoreDir.listFiles(filter)) {
            File f2 = new File(f.getAbsolutePath() + ".save");
            f.renameTo(f2);
            logger.info("Renamed existing property file: " + f.getName() + "->" + f2.getName());
        }
        File masterFile = new File(datastoreDir, "master.properties");
        File slaveFile = new File(datastoreDir, "slave.properties");
        TungstenProperties tp = new TungstenProperties();
        tp.setString("vendor", "some vendor");
        tp.setString("name", "master");
        tp.setString("description", "master datastore");
        tp.setString("driver", nativeDriver);
        tp.setString("url", readwriteUrl);
        tp.setString("role", "master");
        tp.setString("precedence", "1");
        tp.setString("isAvailable", "true");
        FileOutputStream fos1 = new FileOutputStream(masterFile);
        tp.store(fos1);
        fos1.close();
        tp.setString("name", "slave");
        tp.setString("description", "slave datastore");
        tp.setString("url", readonlyUrl);
        tp.setString("role", "slave");
        FileOutputStream fos2 = new FileOutputStream(slaveFile);
        tp.store(fos2);
        fos2.close();
    }
}
