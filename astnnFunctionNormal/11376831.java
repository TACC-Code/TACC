class BackupThread extends Thread {
    public void saveNodeChannel(NodeChannel nodeChannel, boolean reloadChannels) {
        saveChannel(nodeChannel.getChannel(), false);
        saveNodeChannelControl(nodeChannel, reloadChannels);
    }
}
