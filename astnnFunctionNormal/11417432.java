class BackupThread extends Thread {
    protected synchronized URLConnection getURLConnection() throws IOException {
        if (null == _urlConn) {
            final URL url = getURL();
            _urlConn = (null == url) ? null : url.openConnection();
        }
        return _urlConn;
    }
}
