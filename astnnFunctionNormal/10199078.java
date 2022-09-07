class BackupThread extends Thread {
    private void handleSubscriber(WebSocketConnector aConnector, Token aToken) {
        String lEvent = aToken.getString(EVENT);
        if (SUBSCRIBE.equals(lEvent)) {
            subscribe(aConnector, aToken);
        } else if (UNSUBSCRIBE.equals(lEvent)) {
            unsubscribe(aConnector, aToken);
        } else if (GET_CHANNELS.equals(lEvent)) {
            getChannels(aConnector, aToken);
        } else {
            aConnector.stopConnector(CloseReason.CLIENT);
        }
    }
}
