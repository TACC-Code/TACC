class BackupThread extends Thread {
    public InputStream openStream() throws IOException {
        return url.openStream();
    }
}
