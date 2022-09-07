class BackupThread extends Thread {
    private Map<String, String> get(URL url) throws IOException {
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setUseCaches(false);
        return interpretResponse(urlConn);
    }
}
