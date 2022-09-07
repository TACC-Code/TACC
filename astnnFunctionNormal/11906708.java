class BackupThread extends Thread {
    public static ClassInfo read(final URL url) throws IOException, IllegalClassFormatException {
        final InputStream in = url.openStream();
        try {
            return new ClassInfo(in);
        } finally {
            in.close();
        }
    }
}
