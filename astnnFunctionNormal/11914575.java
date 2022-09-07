class BackupThread extends Thread {
    public IChannelSubscribor getChannelBroadcastor(final RemoteName channelName) {
        final InvocationHandler ih = new UnifiedInvocationHandler(m_unifiedMessenger, channelName.getName(), true, channelName.getClazz());
        final IChannelSubscribor rVal = (IChannelSubscribor) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { channelName.getClazz() }, ih);
        return rVal;
    }
}
