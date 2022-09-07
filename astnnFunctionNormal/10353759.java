class BackupThread extends Thread {
    public static URLConnection getURLConnection(final URL url, final String referer) throws IOException {
        final URLConnection con = url.openConnection();
        con.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        con.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        con.addRequestProperty("Accept-Encoding", "gzip,deflate");
        con.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
        con.addRequestProperty("Connection", "keep-alive");
        con.addRequestProperty("Host", url.getHost());
        con.addRequestProperty("Keep-Alive", "300");
        if (referer != null) {
            con.addRequestProperty("Referer", referer);
        }
        con.addRequestProperty("User-Agent", getHttpUserAgent());
        return con;
    }
}
