class BackupThread extends Thread {
    public int receive(ByteArrayOutputStream thisOutStream, AbstractSocketConnection thisSocketConnection) {
        int bytesread = 0;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
            bytesread = ((SocketChannel) thisSocketConnection.mySocketObj).read(buffer);
            if (bytesread > 0) thisOutStream.write(buffer.array(), 0, bytesread);
        } catch (IOException ex) {
            GlobalLog.logError(ex);
        }
        return bytesread;
    }
}
