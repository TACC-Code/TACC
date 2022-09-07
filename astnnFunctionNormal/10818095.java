class BackupThread extends Thread {
    private static void init(SensorQuery query) throws Exception {
        Properties prop = new Properties();
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("cache.conf");
            if (url == null) {
                url = new URL(GlobalConstant.SensorTypeConstants.CACHE_CONF_FILE_URL);
            }
            System.out.println("URL: " + url);
            prop.load(url.openStream());
        } catch (IOException e) {
            System.err.println("can not initial cache manager");
            throw e;
        }
        instance = new CacheManager(prop);
    }
}
