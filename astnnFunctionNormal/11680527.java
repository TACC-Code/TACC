class BackupThread extends Thread {
    public void processMessage(MessageEvent messageEvent) {
        PubSubMessage message = messageEvent.getMessage();
        logger.info(message.getChannelName());
        logger.info(message.getPayload());
        setMessage(message);
    }
}
