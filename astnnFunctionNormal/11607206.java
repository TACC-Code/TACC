class BackupThread extends Thread {
    public void writeTo(final OutputStream outputStream) throws IOException {
        byte[] readBuffer;
        long offset = 0;
        while (offset < this.count) {
            readBuffer = getBytes(offset, BLOCK_BUFFER_SIZE);
            outputStream.write(readBuffer);
            offset += readBuffer.length;
        }
    }
}
