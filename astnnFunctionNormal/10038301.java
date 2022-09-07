class BackupThread extends Thread {
    public static void copy(InputStream in, OutputStream out) throws java.io.IOException {
        byte buffer[] = new byte[COPY_BUFFER_BYTES];
        for (int read = in.read(buffer); read >= 0; read = in.read(buffer)) {
            out.write(buffer, 0, read);
        }
    }
}
