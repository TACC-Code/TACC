class BackupThread extends Thread {
    public static void copyStream(Reader in, Writer out) throws IOException {
        char[] buf = new char[4096];
        int len;
        while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
    }
}
