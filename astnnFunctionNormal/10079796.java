class BackupThread extends Thread {
    public static boolean isInternetConnected() {
        try {
            URL url = new URL("http://tile.openstreetmap.org/");
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            @SuppressWarnings("unused") Object objData = urlConnect.getContent();
            return true;
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }
        return false;
    }
}
