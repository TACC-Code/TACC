class BackupThread extends Thread {
    public URLInputStream(final URL url) throws IOException {
        this(url.openConnection().getInputStream());
    }
}
