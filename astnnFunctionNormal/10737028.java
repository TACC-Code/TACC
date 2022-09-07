class BackupThread extends Thread {
    public void onFailure(Object userContext, Throwable t) {
        logger.debug("#failer.cid:" + getChannelId() + ":" + t.getMessage());
        closeWebSocket("500");
        super.onFailure(userContext, t);
    }
}
