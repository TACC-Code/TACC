class BackupThread extends Thread {
        protected boolean isOsmTileNewer(long fileAge) throws IOException {
            URL url;
            url = new URL(tile.getUrl());
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            prepareHttpUrlConnection(urlConn);
            urlConn.setRequestMethod("HEAD");
            urlConn.setReadTimeout(30000);
            long lastModified = urlConn.getLastModified();
            if (lastModified == 0) return true;
            return (lastModified > fileAge);
        }
}
