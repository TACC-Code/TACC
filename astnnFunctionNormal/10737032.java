class BackupThread extends Thread {
    @Override
    public void onFinished() {
        logger.debug("#finished client.cid:" + getChannelId());
        if (wsProtocol != null) {
            wsProtocol.onClose(WsHybiFrame.CLOSE_UNKOWN, null);
        }
        super.onFinished();
    }
}
