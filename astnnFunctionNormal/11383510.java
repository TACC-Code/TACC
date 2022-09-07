class BackupThread extends Thread {
    private static HttpURLConnection createConnection(String urlString) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("SOAPAction", "");
        conn.setInstanceFollowRedirects(false);
        conn.setUseCaches(false);
        return conn;
    }
}
