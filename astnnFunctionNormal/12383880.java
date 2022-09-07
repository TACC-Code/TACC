class BackupThread extends Thread {
        public void stateChanged(ChangeEvent e) {
            if (!isLocked()) {
                setLocked(true);
                int value = this.track.getTrackBalance().getValue();
                TGChannel channel = this.track.getTrack().getChannel();
                if (value != channel.getBalance()) {
                    channel.setBalance((short) value);
                    fireChannelChanges(this.tracks, channel, BALANCE);
                }
                setLocked(false);
            }
        }
}
