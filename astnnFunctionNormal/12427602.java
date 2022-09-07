class BackupThread extends Thread {
    public void openStream() throws IOException {
        stream = url.openStream();
    }
}
