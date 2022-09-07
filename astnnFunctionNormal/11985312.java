class BackupThread extends Thread {
    public static synchronized boolean isAlive(String strURL) throws MalformedURLException, IOException {
        url = new URL(strURL);
        if (!url.getProtocol().equalsIgnoreCase("HTTP")) {
            throw new IllegalArgumentException("Only support HTTP Protocol");
        }
        urlConn = url.openConnection();
        urlConn.setUseCaches(false);
        urlInputStream = urlConn.getInputStream();
        serverResponse = urlConn.getHeaderField(0);
        if (serverResponse == null) {
            return false;
        }
        if (serverResponse.indexOf("404") >= 0) {
            return false;
        }
        return true;
    }
}
