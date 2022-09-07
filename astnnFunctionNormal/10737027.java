class BackupThread extends Thread {
    public void onReadPlain(Object userContext, ByteBuffer[] buffers) {
        logger.debug("#read.cid:" + getChannelId());
        if (!isWs) {
            super.onReadPlain(userContext, buffers);
            return;
        }
        wsProtocol.onBuffer(buffers);
    }
}
