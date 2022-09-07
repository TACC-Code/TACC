class BackupThread extends Thread {
    public List<Document> parse(final URL url) {
        URLConnection urlConnection = null;
        try {
            urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(Parser.connectTimeout);
            urlConnection.setReadTimeout(Parser.readTimeout);
            urlConnection.addRequestProperty("Connection", "close");
            urlConnection.connect();
            return this.parse(urlConnection.getInputStream(), url);
        } catch (final IOException e) {
            this.treatFetchException(urlConnection);
            return null;
        } finally {
            this.close(urlConnection);
        }
    }
}
