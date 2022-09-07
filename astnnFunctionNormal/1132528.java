class BackupThread extends Thread {
    public OutputStream getOutputStream() throws IOException {
        url_conn = url.openConnection();
        if (url_conn != null) {
            url_conn.setDoOutput(true);
            return url_conn.getOutputStream();
        } else return null;
    }
}
