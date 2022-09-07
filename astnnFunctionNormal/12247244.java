class BackupThread extends Thread {
    public void purge() {
        if (channelAwareComponent != null && channelAwareComponent.getChannels() != null) {
            channelAwareComponent.getChannels().clear();
        }
    }
}
