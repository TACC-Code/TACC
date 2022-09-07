class BackupThread extends Thread {
    private void writeFully(SocketChannel channel, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            int read = channel.write(buffer);
            if (read == -1) {
                throw new IOException();
            }
        }
    }
}
