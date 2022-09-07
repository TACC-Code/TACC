class BackupThread extends Thread {
    protected URLConnection openConnection(URL url) throws IOException {
        try {
            return new RTPURLConnection(url);
        } catch (URISyntaxException ex) {
            throw new IOException("Invalid provided URL");
        }
    }
}
