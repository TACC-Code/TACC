class BackupThread extends Thread {
    public static Properties loadProperty(URL url) throws IOException, FileNotFoundException, URISyntaxException {
        return loadProperty(url.openStream());
    }
}
