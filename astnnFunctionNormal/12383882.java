class BackupThread extends Thread {
        public void actionPerformed(ActionEvent e) {
            if (!isLocked()) {
                setLocked(true);
                TGTrack track = this.track.getTrack();
                track.setSolo(this.track.getTrackSolo().isSelected());
                if (track.isSolo()) {
                    track.setMute(false);
                }
                fireChannelChanges(this.tracks, track.getChannel(), SOLO);
                setLocked(false);
            }
        }
}
