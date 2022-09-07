class BackupThread extends Thread {
    public void cleanup() {
        LOG.debug("--> cleanup()");
        session = null;
        serializer = null;
        port = null;
        setReplyListener(null);
        Channel channel = getChannel();
        if (channel != null) {
            channel.setRequestHandler(null);
        }
        Thread t = getSendingThread();
        if (t != null) {
            LOG.debug("interrupt sending thread [" + t.getName() + "]");
            t.interrupt();
        }
        setChannel(null);
        if (incoming != null) {
            incoming.setRequestHandler(null);
            incoming = null;
        }
        setState(STATE_CLOSED);
        LOG.debug("<-- cleanup()");
    }
}
