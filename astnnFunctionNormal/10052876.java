class BackupThread extends Thread {
    private InputSource getInputSource(String urlString) throws IOException {
        URL url = new URL(urlString);
        return new InputSource(url.openStream());
    }
}
