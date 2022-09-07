class BackupThread extends Thread {
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }
}
