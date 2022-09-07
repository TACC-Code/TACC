class BackupThread extends Thread {
    public DiffSource(final URL url) throws IOException {
        this(url.openStream(), url.toString());
    }
}
