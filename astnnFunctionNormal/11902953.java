class BackupThread extends Thread {
    public IChannelSubscribor getDisplayChannelBroadcaster() {
        final Object implementor = m_game.getChannelMessenger().getChannelBroadcastor(ServerGame.getDisplayChannel(m_data));
        return (IChannelSubscribor) getOutbound(implementor);
    }
}
