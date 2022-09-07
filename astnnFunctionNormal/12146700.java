class BackupThread extends Thread {
    private byte[] toByteArray(final URL url) {
        InputStream input = null;
        try {
            input = url.openStream();
            return IOUtils.toByteArray(input);
        } catch (final Exception eaten) {
            return null;
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
