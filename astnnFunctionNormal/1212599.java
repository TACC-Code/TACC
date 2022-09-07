class BackupThread extends Thread {
    public void run() {
        URL url = null;
        HttpURLConnection connection = null;
        int responseCode = 0;
        try {
            CofaxToolsUtil.log("CofaxToolsClearCache run clearing Cache: " + URLToGet);
            url = new URL(URLToGet);
            connection = (HttpURLConnection) url.openConnection();
            responseCode = connection.getResponseCode();
        } catch (MalformedURLException ex) {
            CofaxToolsUtil.log("CofaxToolsClearCache run ERROR: Malformed URL: " + URLToGet);
        } catch (IOException ex) {
            CofaxToolsUtil.log("CofaxToolsClearCache run ERROR: IO Exception: " + URLToGet);
        }
        if (responseCode != 200) {
            CofaxToolsUtil.log("CofaxToolsClearCache run ERROR: " + responseCode + " " + URLToGet);
        }
    }
}
