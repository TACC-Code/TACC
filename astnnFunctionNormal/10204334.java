class BackupThread extends Thread {
    protected URLConnection openConnection(URL url) throws java.io.IOException {
        return new Connection(url);
    }
}
