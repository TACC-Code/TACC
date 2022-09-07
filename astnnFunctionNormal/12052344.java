class BackupThread extends Thread {
    private static String fetchLatestVersion() {
        String version = null;
        try {
            URL url = new URL("http://jetrix.sourceforge.net/version.php");
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                version = in.readLine();
            } finally {
                conn.disconnect();
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "Unable to check the availability of a new version", e);
        }
        return version;
    }
}
