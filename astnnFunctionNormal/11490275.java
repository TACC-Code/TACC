class BackupThread extends Thread {
    public URLConnection openConnection(URL url) throws IOException, ProtocolNotSuppException {
        return new HTTPClient.HttpURLConnection(url);
    }
}
