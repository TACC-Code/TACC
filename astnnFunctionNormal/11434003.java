class BackupThread extends Thread {
    protected HttpURLConnection doConnect(URL url, String method) throws IOException {
        HttpURLConnection con = null;
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.connect();
        return con;
    }
}
