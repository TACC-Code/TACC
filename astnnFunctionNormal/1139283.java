class BackupThread extends Thread {
    public static void transfer(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024 * 1024];
        int len;
        while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
    }
}
