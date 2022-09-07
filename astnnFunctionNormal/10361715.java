class BackupThread extends Thread {
    private void openConnection(String query, String url) {
        try {
            urlConn = new URL(url + "?" + query).openConnection();
            urlConn.setRequestProperty("Accept-Charset", charset);
            InputStream response = urlConn.getInputStream();
            br = new BufferedReader(new InputStreamReader(response));
        } catch (MalformedURLException e) {
            Log.error(this, e.getMessage());
        } catch (IOException e) {
            Log.error(this, e.getMessage());
        }
    }
}
