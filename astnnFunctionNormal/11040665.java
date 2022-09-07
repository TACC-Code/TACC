class BackupThread extends Thread {
    private synchronized URLConnection getConnection() throws IOException {
        URLConnection c;
        if (conn != null) {
            c = conn;
            conn = null;
        } else {
            c = url.openConnection();
        }
        return c;
    }
}
