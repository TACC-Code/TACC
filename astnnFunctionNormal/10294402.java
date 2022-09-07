class BackupThread extends Thread {
    protected URLConnection openConnection(URL url) throws IOException {
        url = new URL(url, url.toExternalForm(), getDefaultStreamHandler(url.getProtocol()));
        return new NtlmHttpURLConnection((HttpURLConnection) url.openConnection());
    }
}
