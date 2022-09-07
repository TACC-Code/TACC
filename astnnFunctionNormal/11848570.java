class BackupThread extends Thread {
    public URLConnection openConnection() {
        try {
            return this.url.openConnection();
        } catch (IOException e) {
            throw new ConnectException(e);
        }
    }
}
