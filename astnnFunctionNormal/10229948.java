class BackupThread extends Thread {
    public LevelProvider getChannelLevelProvider(final int index) {
        return channelLevelProviders.get(index);
    }
}
