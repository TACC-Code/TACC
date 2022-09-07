class BackupThread extends Thread {
    private void handlePublisher(WebSocketConnector aConnector, Token aToken) {
        String lChannelId = aToken.getString(CHANNEL);
        if (lChannelId == null || EMPTY_STRING.equals(lChannelId)) {
            sendError(aConnector, lChannelId, "Channel value not specified.");
            return;
        }
        if (!aToken.getNS().equals(getNamespace())) {
            sendError(aConnector, lChannelId, "Namespace '" + aToken.getNS() + "' not correct.");
            return;
        }
        String lEvent = aToken.getString(EVENT);
        if (AUTHORIZE.equals(lEvent)) {
            authorize(aConnector, aToken, lChannelId);
        } else if (PUBLISH.equals(lEvent)) {
            Channel lChannel = mChannelManager.getChannel(lChannelId);
            Publisher lPublisher = mChannelManager.getPublisher(aConnector.getSession().getSessionId());
            if (lPublisher == null || !lPublisher.isAuthorized()) {
                sendError(aConnector, lChannelId, "Connector '" + aConnector.getId() + "': access denied, publisher not authorized for channelId '" + lChannelId + "'");
                return;
            }
            String lData = aToken.getString(DATA);
            Token lToken = mChannelManager.getChannelSuccessToken(aConnector, lChannelId, ChannelEventEnum.PUBLISH);
            lToken.setString("data", lData);
            mChannelManager.publishToLoggerChannel(lToken);
            lChannel.broadcastToken(lToken);
        } else if (STOP.equals(lEvent)) {
            Publisher lPublisher = mChannelManager.getPublisher(aConnector.getSession().getSessionId());
            Channel lChannel = mChannelManager.getChannel(lChannelId);
            if (lChannel == null) {
                sendError(aConnector, lChannelId, "'" + aConnector.getId() + "' channel not found for given channelId '" + lChannelId + "'");
                return;
            }
            if (lPublisher == null || !lPublisher.isAuthorized()) {
                sendError(aConnector, lChannelId, "Connector: " + aConnector.getId() + ": access denied, publisher not authorized for channelId '" + lChannelId + "'");
                return;
            }
            try {
                lChannel.stop(lPublisher.getLogin());
                Token lSuccessToken = mChannelManager.getChannelSuccessToken(aConnector, lChannelId, ChannelEventEnum.STOP);
                sendTokenAsync(aConnector, aConnector, lSuccessToken);
            } catch (ChannelLifeCycleException lEx) {
                mLog.error("Error stopping channel '" + lChannelId + "' from publisher " + lPublisher.getId() + "'", lEx);
                Token lErrorToken = mChannelManager.getErrorToken(aConnector, lChannelId, "'" + aConnector.getId() + "' Error stopping channel '" + lChannelId + "' from publisher '" + lPublisher.getId() + "'");
                mChannelManager.publishToLoggerChannel(lErrorToken);
                sendTokenAsync(aConnector, aConnector, lErrorToken);
            }
        }
    }
}
