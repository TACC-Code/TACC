class BackupThread extends Thread {
    public static byte[] readAsByteArray(final InputStream pInput) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nread;
        final byte[] data = new byte[10000];
        while ((nread = pInput.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nread);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
