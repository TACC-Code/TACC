class BackupThread extends Thread {
    private static URLConnection fetchClass0(String host, int port, String filename) throws IOException {
        URL url;
        try {
            url = new URL("http", host, port, filename);
        } catch (MalformedURLException e) {
            throw new IOException("invalid URL?");
        }
        URLConnection con = url.openConnection();
        con.connect();
        return con;
    }
}
