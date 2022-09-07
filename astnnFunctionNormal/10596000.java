class BackupThread extends Thread {
    public void cleanup() {
        LOG.debug("--> cleanup()");
        serializer = null;
        setReplyListener(null);
        Thread t = getSendingThread();
        if (t != null) {
            LOG.debug("interrupt sending thread [" + t.getName() + "]");
            t.interrupt();
        }
        try {
            Channel channel = getChannel();
            if (channel != null) {
                channel.setRequestHandler(null);
                LOG.debug("--> channel.close()");
                channel.close();
                LOG.debug("<-- channel.close()");
            }
        } catch (BEEPException be) {
            LOG.warn("could not close channel [" + be.getMessage() + "]");
        }
        setChannel(null);
        setState(STATE_CLOSED);
        LOG.debug("<-- cleanup()");
    }
}
