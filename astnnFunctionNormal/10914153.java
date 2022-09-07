class BackupThread extends Thread {
    private void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[10240];
        int copied = 0;
        int read;
        read = in.read(buffer);
        while (read != -1) {
            out.write(buffer, 0, read);
            copied += read;
            read = in.read(buffer);
        }
    }
}
