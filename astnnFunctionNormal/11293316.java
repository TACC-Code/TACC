class BackupThread extends Thread {
    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new HttpClientURLConnection(this.httpClient, url);
    }
}
