class BackupThread extends Thread {
    public void applyACTChanAtLevel(ACTAtLevel n) {
        if (!n.hasSelection()) _console.at(int2short(n._level)); else _console.at(n._channels.getChannels(), int2short(n._level));
    }
}
