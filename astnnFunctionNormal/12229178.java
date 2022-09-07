class BackupThread extends Thread {
    public static byte[] getInputBytes(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(in.available());
        byte[] b = new byte[1024];
        int read = 0;
        read = in.read(b);
        while (read >= 0) {
            buffer.write(b, 0, read);
            read = in.read(b);
        }
        return buffer.toByteArray();
    }
}
