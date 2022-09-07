class BackupThread extends Thread {
    public void start() throws Exception {
        _log.trace("starting...");
        _log.info("starting JGroups " + org.jgroups.Version.version);
        super.start();
        init();
        String channelName = getChannelName();
        if (_log.isDebugEnabled()) _log.debug("starting JGroups...: (" + channelName + ")");
        _channel.connect(channelName);
        _log.trace("JGroups Channel connected");
        _dispatcher.start();
        _log.trace("JGroups Dispatcher started");
        if (!_channel.getState(null, getRetrievalTimeOut())) _log.info("cluster state is null - this must be the first node");
        _log.debug("...JGroups started");
        _log.trace("...started");
    }
}
