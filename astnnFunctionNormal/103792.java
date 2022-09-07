class BackupThread extends Thread {
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int read;
        while ((read = in.read(buf, 0, BUFFER_SIZE)) != -1) out.write(buf, 0, read);
    }
}
