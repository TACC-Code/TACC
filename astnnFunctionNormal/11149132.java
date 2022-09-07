class BackupThread extends Thread {
    public Date getLastChannelEventTime(final String groupType) {
        final LoggerSession session = _model.getLoggerSession(groupType);
        if (session != null) {
            return session.getChannelGroup().getLastChannelEventTime();
        } else {
            return new Date(0);
        }
    }
}
