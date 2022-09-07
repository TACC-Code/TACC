class BackupThread extends Thread {
    public void onClosed(Object userContext) {
        logger.debug("#closed client.cid:" + getChannelId());
        super.onClosed(userContext);
    }
}
