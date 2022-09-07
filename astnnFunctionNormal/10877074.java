class BackupThread extends Thread {
    private TGChannel getChannel() {
        return TuxGuitar.instance().getSongManager().getChannel(this.channelId);
    }
}
