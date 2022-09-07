class BackupThread extends Thread {
    public static InputStream getURLAsInputStream(URL url) throws IOException {
        InputStream is = null;
        URLConnection conn = null;
        conn = url.openConnection();
        if (log.isDebugEnabled()) {
            String type = URLConnection.guessContentTypeFromName(url.getPath());
            log.debug("result from guessContentTypeFromName(" + url.getPath() + ") is " + type);
            type = getContentType(conn.getInputStream());
            log.debug("result from getContentType(" + url.getPath() + ") is " + type);
        }
        is = conn.getInputStream();
        return is;
    }
}
