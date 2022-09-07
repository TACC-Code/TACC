class BackupThread extends Thread {
    public synchronized void close() {
        if (closed) {
            return;
        }
        state = STATE_CLOSING;
        closed = true;
        doClose = true;
        log.fine("close " + getConnectionNumber() + " packets in send queue: " + dSendQueue.size() + " packets in receive queue: " + dReceiveQueue.size());
        while (!dSendQueue.isEmpty()) {
            removeOutPacket().disposePacketByteBuffer();
        }
        while (!dReceiveQueue.isEmpty()) {
            getNextPacket().disposePacketByteBuffer();
        }
        this.connected = false;
        thisSecBytesSent = 0;
        thisSecReceived = 0;
        try {
            getChannel().close();
        } catch (IOException e) {
            log.warning(getConnectionNumber() + " Error closing channel: " + e.toString());
        }
        packetDecoder.cleanup();
    }
}
