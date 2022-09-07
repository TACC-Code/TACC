class BackupThread extends Thread {
    public static Properties getProperties(URL url) throws IOException {
        InputStream is = url.openStream();
        Properties properties = getProperties(is);
        is.close();
        return properties;
    }
}
