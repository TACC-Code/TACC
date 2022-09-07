class BackupThread extends Thread {
    public void close() {
        LOG.info("--> close(" + getParticipantId() + ", " + username + ")");
        if (getState() == STATE_ACTIVE) {
            try {
                if (!isKicked && !hasLeft()) {
                    sendSessionTerminated();
                }
                final Thread t = Thread.currentThread();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    public void run() {
                        LOG.debug("going to interrupt [" + t.getName() + "]");
                        t.interrupt();
                        LOG.debug("interrupted.");
                    }
                }, 1500);
                LOG.debug("--> channel.close()");
                getChannel().close();
                timer.cancel();
                LOG.debug("<-- channel.close()");
            } catch (BEEPException be) {
                LOG.warn("could not close channel [" + be.getMessage() + "]");
            }
            executeCleanup();
        } else {
            LOG.warn("cannot close, connection is in state " + getStateString());
        }
        LOG.info("<-- close()");
    }
}
