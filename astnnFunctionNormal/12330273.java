class BackupThread extends Thread {
    public static final Properties getJVMProperties(String propertyName, String propertyValue) throws IOException {
        Properties properties = new Properties();
        if (propertyValue == null) {
            return null;
        } else {
            URL url = null;
            try {
                url = new URL(propertyValue);
                properties.load(url.openStream());
            } catch (IOException e) {
                throw new IOException("Error reading property file: " + e.getMessage());
            }
        }
        return properties;
    }
}
