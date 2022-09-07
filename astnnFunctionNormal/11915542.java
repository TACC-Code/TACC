class BackupThread extends Thread {
    public void setChannel(int channel) {
        accChannel = space.getChannelOffset(channel);
        jump = space.getVerticalSubSampleFactor(channel);
        hCounter = 0;
        wCounter = init = space.getVerticalInit(channel);
    }
}
