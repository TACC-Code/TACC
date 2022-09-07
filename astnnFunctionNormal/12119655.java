class BackupThread extends Thread {
    public InputStream open() throws IOException {
        return _url.openStream();
    }
}
