class BackupThread extends Thread {
    @Override
    public void dispose() {
        server.getChannelManager().removeChannel(getID());
    }
}
