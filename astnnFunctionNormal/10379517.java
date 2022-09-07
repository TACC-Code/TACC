class BackupThread extends Thread {
    public HttpURLConnection getTileUrlConnection(int zoom, int tilex, int tiley) throws IOException {
        String url = getTileUrl(zoom, tilex, tiley);
        if (url == null) return null;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        prepareTileUrlConnection(conn);
        return conn;
    }
}
