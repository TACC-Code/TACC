class BackupThread extends Thread {
    public static ByteArrayOutputStream getByteArrayOutputStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[__copyBufferSize];
        int length = 0;
        while ((length = in.read(buffer, 0, __copyBufferSize)) != -1) out.write(buffer, 0, length);
        try {
            in.close();
        } catch (Exception e) {
        }
        return out;
    }
}
