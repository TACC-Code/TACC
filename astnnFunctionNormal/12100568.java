class BackupThread extends Thread {
    public synchronized void addOutPacket(DonkeyPacket outPacket) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest(getConnectionNumber() + " sending packet " + Convert.byteBufferToHexString(outPacket.getBuffer(), outPacket.getBuffer().position(), Math.min(128, outPacket.getBuffer().remaining())));
        }
        lastActivity = System.currentTimeMillis();
        dSendQueue.add(outPacket);
        if (!writeOff && selectionKey != null && !doClose) {
            if (getChannel().socket().isClosed()) {
                log.log(Level.FINE, getConnectionNumber() + " channel closed! ", new Exception());
            } else {
                try {
                    selectionKey.interestOps(SelectionKey.OP_WRITE | selectionKey.interestOps());
                } catch (java.nio.channels.CancelledKeyException cke) {
                    log.log(Level.WARNING, getConnectionNumber() + " unexpected exception: ", cke);
                }
            }
        }
    }
}
