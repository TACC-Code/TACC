class BackupThread extends Thread {
    public void prepareMask() {
        for (int i = 0; i < getNumberOfChannels(); i++) {
            getChannel(i).prepareMask();
        }
    }
}
