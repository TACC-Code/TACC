class BackupThread extends Thread {
    public void load(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int readCount = 0;
        while ((readCount = inputStream.read(buffer)) > 0) {
            baos.write(buffer, 0, readCount);
        }
        content = baos.toByteArray();
        IOUtils.closeQuietly(baos);
    }
}
