class BackupThread extends Thread {
    public void connect(String method) throws Exception {
        try {
            server = (HttpURLConnection) url.openConnection();
            server.setDoInput(true);
            server.setDoOutput(true);
            server.setRequestMethod(method);
            server.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            server.connect();
        } catch (Exception e) {
            throw new Exception("Connection failed");
        }
    }
}
