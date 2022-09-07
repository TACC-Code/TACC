class BackupThread extends Thread {
    private HttpURLConnection getHttpPostConnection(String _url) throws IOException {
        URL url = new URL(_url);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setUseCaches(false);
        urlcon.setDefaultUseCaches(false);
        urlcon.setDoOutput(true);
        urlcon.setDoInput(true);
        urlcon.setRequestProperty("Content-type", "application/octet-stream");
        urlcon.setAllowUserInteraction(false);
        HttpURLConnection.setDefaultAllowUserInteraction(false);
        urlcon.setRequestMethod("POST");
        return urlcon;
    }
}
