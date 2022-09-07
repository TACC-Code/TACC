class BackupThread extends Thread {
    private synchronized void setUrlConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        this.urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("SOAPAction", urlString);
    }
}
