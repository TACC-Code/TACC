class BackupThread extends Thread {
    private static HttpURLConnection open(String ipAddress) throws IOException {
        URL url = new URL(ipAddress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Host", ipAddress);
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(60000);
        return conn;
    }
}
