class BackupThread extends Thread {
    public void applyACTChanSelect(ACTChanSelect n) {
        _console.select(n._channels.getChannels());
    }
}
