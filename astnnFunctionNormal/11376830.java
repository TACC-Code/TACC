class BackupThread extends Thread {
    public void saveChannel(NodeChannel nodeChannel, boolean reloadChannels) {
        saveChannel(nodeChannel.getChannel(), reloadChannels);
    }
}
