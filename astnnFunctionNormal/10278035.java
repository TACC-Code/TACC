class BackupThread extends Thread {
    public static byte[] getRequestBytes(InputStream request_stream) throws IOException {
        if (request_stream == null) {
            return null;
        }
        int buffer_size = 1024;
        byte[] byte_buffer = new byte[buffer_size];
        int bytes_read = 0;
        ByteArrayOutputStream byte_array_stream = new ByteArrayOutputStream(buffer_size * 2);
        try {
            while ((bytes_read = request_stream.read(byte_buffer)) != -1) {
                byte_array_stream.write(byte_buffer, 0, bytes_read);
            }
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
        return byte_array_stream.toByteArray();
    }
}
