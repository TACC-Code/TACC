class BackupThread extends Thread {
    protected boolean hasChannelChanges(TGTrackImpl track, int channelId) {
        return (track.getChannelId() != channelId);
    }
}
