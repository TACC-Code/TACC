class BackupThread extends Thread {
    private InputStreamReader getInputStreamReader(String uri) throws MalformedURLException, IOException {
        URL url = new URL(uri);
        URLConnection connection = url.openConnection();
        return new InputStreamReader(connection.getInputStream());
    }
}
