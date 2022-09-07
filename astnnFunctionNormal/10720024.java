class BackupThread extends Thread {
    public InputStream openInputStream() throws IOException {
        return url.openStream();
    }
}
