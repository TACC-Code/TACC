class BackupThread extends Thread {
    @Ignore
    public static String getFileContents(File file) throws IOException {
        FileInputStream fis = null;
        FileChannel input = null;
        StringBuilder sBuilder = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            input = fis.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(64);
            int bytesRead = input.read(byteBuffer);
            while (bytesRead != -1) {
                byteBuffer.flip();
                CharBuffer charBuffer = byteBuffer.asCharBuffer();
                sBuilder.append(charBuffer.toString());
                bytesRead = input.read(byteBuffer);
            }
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fis);
        }
        return sBuilder.toString();
    }
}
