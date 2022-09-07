class BackupThread extends Thread {
    protected URLConnection openConnection(URL url) throws IOException {
        return new AddeURLConnection(url);
    }
}
