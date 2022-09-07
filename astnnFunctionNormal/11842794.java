class BackupThread extends Thread {
    private static InputStream openStream(URL url) throws IOException {
        final InputStream is = url.openStream();
        if (is == null) throw new FileNotFoundException(url.toExternalForm());
        return is;
    }
}
