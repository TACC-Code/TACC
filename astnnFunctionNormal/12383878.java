class BackupThread extends Thread {
        public void stateChanged(ChangeEvent e) {
            if (!isLocked()) {
                setLocked(true);
                int value = this.track.getTrackVolume().getValue();
                TGChannel channel = this.track.getTrack().getChannel();
                if (value != channel.getVolume()) {
                    channel.setVolume((short) value);
                    fireChannelChanges(this.tracks, channel, VOLUME);
                }
                setLocked(false);
            }
        }
}
