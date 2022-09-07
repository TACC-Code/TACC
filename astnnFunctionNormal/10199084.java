class BackupThread extends Thread {
    private void getChannels(WebSocketConnector aConnector, Token aToken) {
        if (mLog.isDebugEnabled()) {
            mLog.debug("Processing 'getChannels'...");
        }
        Token lResponseToken = createResponse(aToken);
        List lPublic = new FastList();
        lPublic.addAll(mChannelManager.getPublicChannels().keySet());
        lResponseToken.setList("publicChannels", lPublic);
        List lSystem = new FastList();
        lSystem.addAll(mChannelManager.getSystemChannels().keySet());
        lResponseToken.setList("systemChannels", lSystem);
        List lPrivate = new FastList();
        lPrivate.addAll(mChannelManager.getPrivateChannels().keySet());
        lResponseToken.setList("privateChannels", lPrivate);
        sendToken(aConnector, aConnector, lResponseToken);
    }
}
