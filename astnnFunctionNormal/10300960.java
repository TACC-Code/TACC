class BackupThread extends Thread {
    public static byte[] readURL(final URL url) throws IOException {
        return readInputStream(url.openStream());
    }
}
