class BackupThread extends Thread {
    public synchronized void processOutput() {
        callCounterOut++;
        DonkeyUpLoadLimiter.Limiter.decConnections();
        int sendBytesInWhile = DonkeyUpLoadLimiter.Limiter.getMaxBytesPerConnection();
        writeOff = sendBytesInWhile == 0;
        while ((hasOutput() || hasOutBuffer()) && getChannel().isOpen() && sendBytesInWhile > 0 && !writeOff) {
            if (!hasOutBuffer()) {
                outPacket = getNextOutPacket();
                currentOpcode = outPacket.getBuffer().remaining() > 5 ? outPacket.getCommandId() : 0;
                setOutBuffer(outPacket.getBuffer());
                setHasOutBuffer(true);
                removeOutPacket();
            }
            ByteBuffer buf = getOutBuffer();
            int limit = buf.limit();
            int maxwrite = buf.position() + sendBytesInWhile;
            buf.limit(maxwrite > limit ? limit : maxwrite);
            try {
                int nbytes = getChannel().write(buf);
                sendBytesInWhile -= nbytes;
                if (nbytes > 0) {
                    lastActivity = System.currentTimeMillis();
                    addStatistic(currentOpcode, DIRECTION_OUT, nbytes);
                }
                addSentBytesNum(nbytes);
                buf.limit(limit);
                if (buf.remaining() == 0) {
                    log.finest(getConnectionNumber() + " sent " + Convert.byteToHex(outPacket.getCommandId()));
                    setHasOutBuffer(false);
                    setOutBuffer(null);
                    outPacket.disposePacketByteBuffer();
                } else {
                    break;
                }
            } catch (IOException e) {
                log.fine(getConnectionNumber() + " Error sending...: " + e.getMessage());
                try {
                    getChannel().close();
                } catch (IOException e2) {
                }
                doClose = true;
                return;
            }
        }
        if (hasOutput()) {
            if (writeOff) {
                selectionKey.interestOps(SELECTION_MASK_WRITE_OFF & selectionKey.interestOps());
                writeOff = false;
                writeOn = true;
            }
        } else {
            if (writeOff) {
                writeOn = true;
            }
            selectionKey.interestOps(SELECTION_MASK_WRITE_OFF & selectionKey.interestOps());
        }
    }
}
