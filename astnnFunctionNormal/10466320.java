class BackupThread extends Thread {
    public void markChange() {
        for (int i = 0; i < getNumberOfChannels(); i++) {
            getChannel(i).markChange();
        }
    }
}
