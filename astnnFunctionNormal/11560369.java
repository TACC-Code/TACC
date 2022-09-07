class BackupThread extends Thread {
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte buf[] = new byte[4096];
        int i;
        while ((i = in.read(buf)) != -1) out.write(buf, 0, i);
    }
}
