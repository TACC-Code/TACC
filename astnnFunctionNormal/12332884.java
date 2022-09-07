class BackupThread extends Thread {
    public static void write(Writer writer, Reader reader) throws IOException {
        if (null == writer || null == reader) return;
        char[] cbuf = new char[BUF_SIZE];
        int len;
        while (-1 != (len = reader.read(cbuf))) {
            writer.write(cbuf, 0, len);
        }
    }
}
