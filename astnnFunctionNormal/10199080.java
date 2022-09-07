class BackupThread extends Thread {
    private void authorize(WebSocketConnector aConnector, Token aToken, String aChannelId) {
        String lAccessKey = aToken.getString(ACCESS_KEY);
        String lSecretKey = aToken.getString(SECRET_KEY);
        String lLogin = aToken.getString("login");
        User lUser = SecurityFactory.getUser(lLogin);
        if (lUser == null) {
            sendError(aConnector, aChannelId, "'" + aConnector.getId() + "' Authorization failed for channel '" + aChannelId + "', channel owner is not registered in the jWebSocket server system");
            return;
        }
        if (lSecretKey == null || lAccessKey == null) {
            sendError(aConnector, aChannelId, "'" + aConnector.getId() + "' Authorization failed, secret_key/access_key pair value is not correct");
            return;
        } else {
            Channel lChannel = mChannelManager.getChannel(aChannelId);
            if (lChannel == null) {
                sendError(aConnector, aChannelId, "'" + aConnector.getId() + "' channel not found for given channelId '" + aChannelId + "'");
                return;
            }
            Publisher lPublisher = authorizePublisher(aConnector, lChannel, lUser, lSecretKey, lAccessKey);
            if (!lPublisher.isAuthorized()) {
                sendError(aConnector, aChannelId, "'" + aConnector.getId() + "' Authorization failed for channel '" + aChannelId + "'");
            } else {
                lChannel.addPublisher(lPublisher);
                Token lResponseToken = mChannelManager.getChannelSuccessToken(aConnector, aChannelId, ChannelEventEnum.AUTHORIZE);
                mChannelManager.publishToLoggerChannel(lResponseToken);
                sendToken(aConnector, aConnector, lResponseToken);
            }
        }
    }
}
