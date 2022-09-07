class BackupThread extends Thread {
    public InputStream get(final String id) throws MalformedURLException, IOException {
        final String url = DOWNLOAD_BASE_URL + id + URL_SEPARATOR + id + EXTENSION;
        return new URL(url).openStream();
    }
}
