class BackupThread extends Thread {
    public void append(int chan, float value) {
        getChannel(chan)[writeIndex] = value;
    }
}
