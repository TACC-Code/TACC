class BackupThread extends Thread {
    private void updateHTTP(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        long lastModified = conn.getHeaderFieldDate("last-modified", -1L);
        if (lastModified == -1L) {
            throw new IOException("Last-Modified: header missing");
        }
        url2meta.put(url, new Long(lastModified));
    }
}
