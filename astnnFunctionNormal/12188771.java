class BackupThread extends Thread {
    public void onFinished() {
        logger.debug("#finished.cid:" + getChannelId());
        super.onFinished();
    }
}
