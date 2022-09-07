class BackupThread extends Thread {
    public static String getContentAsString(final URL url, final String draftEncoding) throws IOException {
        Validate.notNull(url, "URL must not be null.");
        final String encoding = draftEncoding == null ? "UTF-8" : draftEncoding;
        InputStream inputStream = null;
        try {
            final URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            return IOUtils.toString(inputStream, encoding);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
