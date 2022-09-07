class BackupThread extends Thread {
    public static Image createImage(InputStream stream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8 * 1024];
        int read;
        while ((read = stream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        buffer = out.toByteArray();
        return createImage(buffer, 0, buffer.length);
    }
}
