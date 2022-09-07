class BackupThread extends Thread {
    private boolean checkConnectivity(boolean isLocalHost) {
        boolean connected = false;
        try {
            URL url = new URL("http://www.google.com");
            if (isLocalHost) {
                url = new URL(PlagiabustServerManager.getSolrURL());
            }
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            if (urlConnect.getResponseCode() == HttpURLConnection.HTTP_OK) {
                connected = true;
            }
        } catch (Exception ex) {
        }
        return connected;
    }
}
