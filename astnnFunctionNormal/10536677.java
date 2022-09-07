class BackupThread extends Thread {
    public void connect(String endpoint) throws Exception {
        this.endpoint = endpoint;
        parse_endpoint();
        URL url = new URL(protocol, host, Integer.parseInt(port), service);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
    }
}
