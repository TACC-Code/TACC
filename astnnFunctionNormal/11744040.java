class BackupThread extends Thread {
    public DefaultContent(final URL url) throws IOException {
        super(url);
        this.connection = url.openConnection();
        lastModified = connection.getLastModified();
    }
}
