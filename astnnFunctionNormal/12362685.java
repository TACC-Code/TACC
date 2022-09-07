class BackupThread extends Thread {
    private boolean nioHandleWrite(final SelectionKey writeKey, final ByteBuffer writeBuffer) {
        final SocketChannel socketChannel = (SocketChannel) writeKey.channel();
        int numWritten;
        try {
            numWritten = socketChannel.read(writeBuffer);
        } catch (final IOException e) {
            return false;
        }
        if (numWritten == -1) {
            return false;
        }
        return true;
    }
}
