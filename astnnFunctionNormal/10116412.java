class BackupThread extends Thread {
    public URLOutputStream(final URL url) throws IOException {
        this(url.openConnection().getOutputStream());
    }
}
