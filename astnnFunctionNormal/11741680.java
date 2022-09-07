class BackupThread extends Thread {
    public static void transfer(Reader in, Writer out) throws IOException {
        char[] buf = new char[BUFFER_SIZE];
        while (true) {
            int read = in.read(buf, 0, BUFFER_SIZE);
            if (read == -1) break;
            out.write(buf, 0, read);
        }
    }
}
