class BackupThread extends Thread {
    @Override
    public void connectorStopped(WebSocketConnector aConnector, CloseReason aCloseReason) {
        Subscriber lSubscriber = mChannelManager.getSubscriber(aConnector.getId());
        for (String lChannelId : lSubscriber.getChannels()) {
            Channel lChannel = mChannelManager.getChannel(lChannelId);
            if (lChannel != null) {
                lChannel.unsubscribe(lSubscriber, mChannelManager);
            }
        }
        mChannelManager.removeSubscriber(lSubscriber);
    }
}
