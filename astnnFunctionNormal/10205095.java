class BackupThread extends Thread {
    private static String readerToString(final Reader reader) throws IOException {
        assert reader != null : "null reader";
        final int SIZE = 1024;
        final StringWriter res = new StringWriter(SIZE);
        final char[] buffer = new char[SIZE];
        int read = reader.read(buffer);
        while (read != -1) {
            res.write(buffer, 0, read);
            read = reader.read(buffer);
        }
        return res.toString();
    }
}
