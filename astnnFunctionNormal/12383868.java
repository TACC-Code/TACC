class BackupThread extends Thread {
    private void fireUpdate(TrackPanel track, int type) {
        TGChannel channel = track.getTrack().getChannel();
        if ((type & SOLO) != 0 || (type & MUTE) != 0) {
            track.getTrackSolo().setSelected(track.getTrack().isSolo());
            track.getTrackMute().setSelected(track.getTrack().isMute());
        }
        if ((type & VOLUME) != 0) {
            track.getTrackVolume().setValue(channel.getVolume());
            track.getTrackVolumeValue().setText(Integer.toString(channel.getVolume()));
        }
        if ((type & BALANCE) != 0) {
            track.getTrackBalance().setValue(channel.getBalance());
        }
    }
}
