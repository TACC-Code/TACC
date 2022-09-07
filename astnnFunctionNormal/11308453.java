class BackupThread extends Thread {
    private Reader openURL(URL url) {
        if (m_cp.getM_verbose()) System.out.println("Trying to open url '" + url + "'...");
        try {
            java.net.URLConnection conn = url.openConnection();
            conn.connect();
            if (conn instanceof java.net.HttpURLConnection) {
                java.net.HttpURLConnection hConn = (java.net.HttpURLConnection) url.openConnection();
                if (hConn.getResponseCode() != HttpURLConnection.HTTP_OK) return null;
            }
            if (m_cp.getM_verbose()) System.out.println("Successfully opened url '" + url + "'.");
            return new InputStreamReader(conn.getInputStream());
        } catch (IOException ex) {
        }
        if (m_cp.getM_verbose()) System.out.println("Failed to open url '" + url + "'.");
        return null;
    }
}
