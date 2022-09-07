class BackupThread extends Thread {
    public static Reader getURLAsReader(URL url) throws IOException {
        InputStreamReader isr = null;
        URLConnection conn = null;
        String encoding = "UTF-8";
        conn = url.openConnection();
        if (log.isDebugEnabled()) {
            String type = URLConnection.guessContentTypeFromName(url.getPath());
            log.debug("result from guessContentTypeFromName(" + url.getPath() + ") is " + type);
            type = getContentType(conn.getInputStream());
        }
        isr = new InputStreamReader(conn.getInputStream(), encoding);
        return isr;
    }
}
