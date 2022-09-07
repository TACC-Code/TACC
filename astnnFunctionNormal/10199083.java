class BackupThread extends Thread {
    private void unsubscribe(WebSocketConnector aConnector, Token aToken) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("Processing 'unsubscribe'...");
        }
        String lChannelId = aToken.getString(CHANNEL);
        if (lChannelId == null || EMPTY_STRING.equals(lChannelId)) {
            sendError(aConnector, null, "Channel value is null");
            return;
        }
        Token lResponseToken = createResponse(aToken);
        Subscriber lSubscriber = mChannelManager.getSubscriber(aConnector.getId());
        if (lSubscriber != null) {
            Channel lChannel = mChannelManager.getChannel(lChannelId);
            if (lChannel != null) {
                lChannel.unsubscribe(lSubscriber, mChannelManager);
                lResponseToken.setString("unsubscribe", "ok");
            }
        } else {
            lResponseToken.setInteger("code", -1);
            lResponseToken.setString("msg", "Client not subscribed to channel '" + lChannelId + "'.");
        }
        sendToken(aConnector, aConnector, lResponseToken);
    }
}
