class BackupThread extends Thread {
    public byte[] getData() throws IOException {
        InputStream stream = getInputStream();
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read;
        while ((read = stream.read(buffer)) > 0) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }
}
