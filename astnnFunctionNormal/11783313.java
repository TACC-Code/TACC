class BackupThread extends Thread {
    public void applyACTMinus(ACTMinus n) {
        if (!n.hasSelection()) _console.minus(int2short(n._level)); else _console.minus(n._channels.getChannels(), int2short(n._level));
    }
}
