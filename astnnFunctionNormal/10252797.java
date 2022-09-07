class BackupThread extends Thread {
    public void registerKeys() {
        try {
            acceptKey = getChannel().register(getSelector(), SelectionKey.OP_ACCEPT);
            acceptKey.attach(this);
        } catch (ClosedChannelException e) {
            logger.warning(e.toString());
        }
    }
}
