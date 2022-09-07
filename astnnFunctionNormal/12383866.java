class BackupThread extends Thread {
    public void fireChannelChanges(TrackPanel[] trackPanels, TGChannel channel, int type) {
        Iterator it = getSongManager().getSong().getTracks();
        while (it.hasNext()) {
            TGTrack track = (TGTrack) it.next();
            if (track.getChannel().getChannel() == channel.getChannel()) {
                channel.copy(track.getChannel());
            }
        }
        if (TuxGuitar.instance().getPlayer().isRunning()) {
            TuxGuitar.instance().getPlayer().updateControllers();
        }
        fireUpdate(trackPanels, type);
    }
}
