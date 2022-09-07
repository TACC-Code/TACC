class BackupThread extends Thread {
    public void applyACTPlus(ACTPlus n) {
        if (!n.hasSelection()) _console.plus(int2short(n._level)); else _console.plus(n._channels.getChannels(), int2short(n._level));
    }
}
