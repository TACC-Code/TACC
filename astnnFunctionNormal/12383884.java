class BackupThread extends Thread {
        public void actionPerformed(ActionEvent e) {
            if (!isLocked()) {
                setLocked(true);
                TGTrack track = this.track.getTrack();
                track.setMute(this.track.getTrackMute().isSelected());
                if (track.isMute()) {
                    track.setSolo(false);
                }
                fireChannelChanges(this.tracks, track.getChannel(), MUTE);
                setLocked(false);
            }
        }
}
