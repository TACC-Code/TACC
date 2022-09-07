class BackupThread extends Thread {
    protected boolean shouldTransposeStrings(TGTrackImpl track, int selectedChannelId) {
        if (this.stringTransposition.getSelection()) {
            boolean percussionChannelNew = getSongManager().isPercussionChannel(selectedChannelId);
            boolean percussionChannelOld = getSongManager().isPercussionChannel(track.getChannelId());
            return (!percussionChannelNew && !percussionChannelOld);
        }
        return false;
    }
}
