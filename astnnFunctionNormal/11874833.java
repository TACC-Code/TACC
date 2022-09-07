class BackupThread extends Thread {
    public URLConnection openConnection(URL url) throws IOException {
        ResURLConnection con = new ResURLConnection(url);
        con.connect();
        return con;
    }
}
