class BackupThread extends Thread {
    public void modifySelection(int offset, int length) {
        for (int i = 0; i < getNumberOfChannels(); i++) {
            getChannel(i).modifySelection(offset, length);
        }
    }
}
