class BackupThread extends Thread {
    @Override
    public HttpURLConnection getHttpConnection(String sUrl, int timeout) throws MalformedURLException, IOException {
        URL url = new URL(sUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(timeout);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Accept", "text/*,application/xml,application/xhtml+xml");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
        conn.connect();
        return conn;
    }
}
