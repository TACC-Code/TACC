class BackupThread extends Thread {
    private byte[] uncompress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        uncompressor.setInput(data);
        try {
            byte[] buf = new byte[1024];
            while (true) {
                int read = uncompressor.inflate(buf);
                if (read == 0) break;
                baos.write(buf, 0, read);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (DataFormatException e) {
            throw new IOException("Bad deflate format");
        }
    }
}
