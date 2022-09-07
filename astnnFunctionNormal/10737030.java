class BackupThread extends Thread {
    public void onTimeout(Object userContext) {
        logger.debug("#timeout.cid:" + getChannelId());
        closeWebSocket("500");
        super.onTimeout(userContext);
    }
}
