class BackupThread extends Thread {
    public KeepAliveContext getKeepAliveContext(boolean isCreate) {
        if (getChannelId() < 0) {
            return null;
        }
        KeepAliveContext keepAliveContext = (KeepAliveContext) getAttribute(WebServerHandler.ATTRIBUTE_KEEPALIVE_CONTEXT);
        if (isCreate && keepAliveContext == null) {
            keepAliveContext = (KeepAliveContext) PoolManager.getInstance(KeepAliveContext.class);
            keepAliveContext.setAcceptServer(ServerParser.create(getLocalIp(), getLocalPort()));
            setKeepAliveContext(keepAliveContext);
        }
        return keepAliveContext;
    }
}
