class BackupThread extends Thread {
    public void stop() {
        _log.trace("stopping...");
        _timer.cancel();
        _log.trace("Touch Timer stopped");
        if (_log.isDebugEnabled()) _log.debug("stopping JGroups...: (" + getChannelName() + ")");
        _dispatcher.stop();
        _log.trace("JGroups RpcDispatcher stopped");
        _channel.disconnect();
        _log.trace("JGroups Channel disconnected");
        _log.debug("...JGroups stopped");
        super.stop();
        _log.trace("...stopped");
    }
}
