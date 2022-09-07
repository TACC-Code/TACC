class BackupThread extends Thread {
    public SetupHelper() throws Exception {
        String testPropertiesName = System.getProperty("test.properties");
        if (testPropertiesName == null) {
            testPropertiesName = "test.properties";
            logger.info("Setting test.properties file name to default: test.properties");
        }
        TungstenProperties tp = new TungstenProperties();
        File f = new File(testPropertiesName);
        if (f.canRead()) {
            logger.info("Reading test.properties file: " + testPropertiesName);
            FileInputStream fis = new FileInputStream(f);
            tp.load(fis);
            fis.close();
        } else logger.warn("Using default values for test");
        nativeDriver = tp.getString("test.native.driver", "org.apache.derby.jdbc.EmbeddedDriver", true);
        readwriteUrl = tp.getString("test.readwrite.url", "jdbc:derby:readwrite;create=true", true);
        readonlyUrl = tp.getString("test.readonly.url", "jdbc:derby:readonly;create=true", true);
        user = tp.getString("test.database.user");
        password = tp.getString("test.database.password");
        String routerHomeName = System.getProperty("router.home");
        if (routerHomeName == null) throw new Exception("Property router.home is not set");
        File routerHome = new File(routerHomeName);
        if (!routerHome.exists()) {
            logger.info("Creating missing router.home directory: " + routerHome.getAbsolutePath());
            routerHome.mkdirs();
        }
        if (!routerHome.isDirectory() || !routerHome.canRead()) throw new Exception("Directory router.home invalid or unreadable: " + routerHome.getAbsolutePath());
        routerConf = new File(routerHome, "conf");
        if (!routerConf.exists()) {
            logger.info("Creating missing router.home/conf directory: " + routerConf.getAbsolutePath());
            routerConf.mkdirs();
        }
        if (!routerConf.isDirectory() || !routerConf.canWrite()) throw new Exception("Directory router.home/conf invalid or unreadable: " + routerConf.getAbsolutePath());
        Column colDbtype = new Column();
        colDbtype.setName("dbtype");
        colDbtype.setType(Types.VARCHAR);
        colDbtype.setLength(40);
        Column[] cols = new Column[] { colDbtype };
        tableDbtype = new Table("dbtype_table", cols);
        createDbtypeTable(this.readwriteUrl, "rw");
        createDbtypeTable(this.readonlyUrl, "ro");
    }
}
