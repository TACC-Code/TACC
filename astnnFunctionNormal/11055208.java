class BackupThread extends Thread {
    public ClientHttpRequest(URL url) throws IOException {
        this((HttpURLConnection) url.openConnection());
    }
}
