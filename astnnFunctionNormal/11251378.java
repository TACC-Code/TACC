class BackupThread extends Thread {
    public boolean handleWrite() {
        if (currentState != null) {
            if (currentState.isReading()) {
                LOG.warn("Got a write notification while reading");
                writeSink.interest(this, false);
                return false;
            } else {
                return processCurrentState(currentState, false);
            }
        } else {
            LOG.warn("Got a write notification with no current state");
            writeSink.interest(this, false);
            return false;
        }
    }
}
