class BackupThread extends Thread {
    public static long lastMod(final String uri) {
        try {
            final URL url = new URL(uri);
            final URLConnection con = url.openConnection();
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).setRequestMethod("HEAD");
            }
            return con.getLastModified();
        } catch (final IOException ignore) {
            return 0L;
        }
    }
}
