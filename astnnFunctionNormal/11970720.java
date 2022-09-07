class BackupThread extends Thread {
    public static Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try {
            URL url = ClassLoader.getSystemResource(fileName);
            InputStream in = url.openStream();
            props.load(in);
            return props;
        } catch (Exception e) {
            System.out.println("Warning: Cannot load property file " + PROPERTY_FILE_NAME + ". " + e.getMessage());
            return props;
        }
    }
}
