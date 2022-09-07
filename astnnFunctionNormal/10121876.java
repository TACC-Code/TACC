class BackupThread extends Thread {
    private HttpURLConnection getConnection() throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(SERVICE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }
}
