class BackupThread extends Thread {
    static HttpURLConnection createConnection(String uri) throws MalformedURLException, IOException {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", AbstractRestfulClient.MIME_TYPE);
        return connection;
    }
}
