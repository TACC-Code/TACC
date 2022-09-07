class BackupThread extends Thread {
    private void subscribe(WebSocketConnector aConnector, Token aToken) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("Processing 'subscribe'...");
        }
        String lChannelId = aToken.getString(CHANNEL);
        if (lChannelId == null || EMPTY_STRING.equals(lChannelId)) {
            sendError(aConnector, null, "Channel value is null");
            return;
        }
        String lAccessKey = aToken.getString(ACCESS_KEY);
        Channel lChannel = mChannelManager.getChannel(lChannelId);
        if (lChannel == null || lChannel.getState() == Channel.ChannelState.STOPPED) {
            sendError(aConnector, lChannelId, "Channel doesn't exists for the channelId: '" + lChannelId + "'");
            return;
        }
        if (lChannel.isPrivateChannel() && EMPTY_STRING.equals(lAccessKey)) {
            sendError(aConnector, lChannelId, "Access_key required for subscribing to a private channel: '" + lChannelId + "'");
            return;
        }
        if (lChannel.isPrivateChannel() && !EMPTY_STRING.equals(lAccessKey)) {
            if (lChannel.getAccessKey().equals(lAccessKey)) {
                sendError(aConnector, lChannelId, "Access_key not valid for the given channel id: '" + lChannelId + "'");
                return;
            }
        }
        Subscriber lSubscriber = mChannelManager.getSubscriber(aConnector.getId());
        Date lDate = new Date();
        if (lSubscriber == null) {
            lSubscriber = new Subscriber(aConnector, getServer(), lDate);
        }
        Token lResponseToken = createResponse(aToken);
        if (lSubscriber.getChannels().contains(lChannelId)) {
            lResponseToken.setInteger("code", -1);
            lResponseToken.setString("msg", "Client already subscribed to channel '" + lChannelId + "'.");
        } else {
            lChannel.subscribe(lSubscriber, mChannelManager);
            lResponseToken.setString("subscribe", "ok");
        }
        sendToken(aConnector, aConnector, lResponseToken);
    }
}
