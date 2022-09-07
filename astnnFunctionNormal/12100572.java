class BackupThread extends Thread {
    public boolean check(int count) {
        if (doClose) close();
        if (getChannel().socket().isClosed()) {
            log.log(Level.FINEST, getConnectionNumber() + " " + (System.currentTimeMillis() - transferStart) + " socket closed!", new Exception());
            close();
            return false;
        }
        if ((count << 1) % 5 == 0) {
            calcSpeed();
            if (count % 20 == 0) {
                if (!waiting) {
                    if ((System.currentTimeMillis() - lastActivity) > (getTimeOut() * MiscUtil.TU_MillisPerSecond)) {
                        log.finest(getConnectionNumber() + " Connection timeout. State: " + state);
                        close();
                        return false;
                    }
                }
            }
        }
        if (readOn) {
            readOn = false;
            log.fine(getConnectionNumber() + " read on " + ConnectionManager.getInstance().getLoopCount());
            selectionKey.interestOps(SelectionKey.OP_READ | selectionKey.interestOps());
        }
        if (writeOn && hasOutput()) {
            writeOn = false;
            selectionKey.interestOps(SelectionKey.OP_WRITE | selectionKey.interestOps());
        }
        return (true);
    }
}
