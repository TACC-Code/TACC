class BackupThread extends Thread {
    private static String getChars(Reader r) throws IOException {
        if (r == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
        StringWriter sw = new StringWriter(10 * 1024);
        char[] c = new char[4096];
        while (true) {
            int read = r.read(c);
            if (read == -1) {
                break;
            }
            sw.write(c, 0, read);
        }
        sw.close();
        return sw.toString();
    }
}
