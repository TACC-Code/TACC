class BackupThread extends Thread {
    @SuppressWarnings("deprecation")
    private static void actuallySendPing(String app, String version, long id) throws IOException {
        String os = System.getProperty("os.name");
        if (os.startsWith("Mac OS")) {
            os = "mac";
            String osVers = getVersion();
            if (osVers != null) {
                os = os + "-" + osVers;
            }
        } else if (os.startsWith("Windows")) {
            os = "win";
            String osVers = getVersion();
            if (osVers != null) {
                os = os + "-" + osVers;
            }
        } else if (os.startsWith("Linux")) {
            os = "linux";
        } else {
            os = URLEncoder.encode(os);
        }
        URL url = new URL("http", "tools.google.com", "/service/update?as=androidsdk_" + app + "&id=" + Long.toHexString(id) + "&version=" + version + "&os=" + os);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK && conn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
            throw new IOException(conn.getResponseMessage() + ": " + url);
        }
    }
}
