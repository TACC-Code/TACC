class BackupThread extends Thread {
    public int getChannelMode(int channelNumber) {
        return dmaRegs[channelNumber].mode;
    }
}
