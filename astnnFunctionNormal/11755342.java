class BackupThread extends Thread {
    public static InputStream problemInputStream(final URL url) throws IOException {
        final String path = url.getPath();
        if (path.endsWith(".gz")) {
            return new GZIPInputStream(url.openStream());
        }
        if (path.endsWith(".bz2")) {
            final InputStream is = url.openStream();
            is.read();
            is.read();
            return new CBZip2InputStream(is);
        }
        return url.openStream();
    }
}
