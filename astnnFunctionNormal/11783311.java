class BackupThread extends Thread {
    public void applyACTOut(ACTOut n) {
        if (!n.hasSelection()) _console.out(); else _console.out(n._channels.getChannels());
    }
}
