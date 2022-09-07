class BackupThread extends Thread {
    public static Collection<String> getAllMatches(final URL url, final String regex, final int flags) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            return getAllMatches(is, regex, flags);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
