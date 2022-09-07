class BackupThread extends Thread {
    private static void init() {
        Properties properties = null;
        try {
            URL url = ClassLoader.getSystemResource(PROPERTIES_FILE_NAME);
            if (url != null) {
                properties = new Properties();
                properties.load(url.openStream());
            }
        } catch (IOException ioe) {
            System.err.println("Error when loading logging properties: " + ioe);
        }
        if ((properties == null) || (!properties.containsKey(PROPERTY_KEY_LOGLEVEL))) {
            System.out.println("Using default logging configuration. You can add \"" + PROPERTIES_FILE_NAME + "\" file to the classpath to override." + " See http://code.google.com/p/redmine-java-api/issues/detail?id=95");
            properties = createDefaultConfiguration();
        }
        try {
            logLevel = LogLevel.valueOf((String) properties.get(PROPERTY_KEY_LOGLEVEL));
        } catch (IllegalArgumentException iae) {
            System.err.println("Invalid value for " + PROPERTY_KEY_LOGLEVEL + " specified in logging configuration " + PROPERTIES_FILE_NAME + " => using default log level " + DEFAULT_LOG_LEVEL);
            logLevel = DEFAULT_LOG_LEVEL;
        }
    }
}
