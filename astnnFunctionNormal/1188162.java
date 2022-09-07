class BackupThread extends Thread {
    public static InputStream getResourceInputStream(String pluginRelativePath) throws IOException {
        URL url = getResourceUrl(pluginRelativePath);
        return url.openStream();
    }
}
