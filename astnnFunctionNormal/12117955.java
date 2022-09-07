class BackupThread extends Thread {
    private void sendFile(String filename, SocketChannel sock) throws IOException {
        if (filename.isEmpty()) {
            return;
        }
        FileInputStream is = new FileInputStream(new File(filename));
        FileChannel source = is.getChannel();
        ByteBuffer sendBuf = ByteBuffer.allocateDirect(fileSendingChunk);
        while (source.read(sendBuf) > 0) {
            sendBuf.flip();
            if (log.isDebugEnabled()) {
                log.debug("Sending " + sendBuf);
            }
            sock.write(sendBuf);
            sendBuf.rewind();
        }
    }
}
